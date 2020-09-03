package remix.myplayer.service.notification

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.app.NotificationCompat
import android.view.View
import android.widget.RemoteViews
import remix.myplayer.R
import remix.myplayer.request.RemoteUriRequest
import remix.myplayer.request.RequestConfig
import remix.myplayer.service.Command
import remix.myplayer.service.MusicService
import remix.myplayer.util.ColorUtil
import remix.myplayer.util.DensityUtil
import remix.myplayer.util.ImageUriUtil.getSearchRequestWithAlbumType
import remix.myplayer.util.LogUtils
import remix.myplayer.util.SPUtil


/**
 * Created by Remix on 2017/11/22.
 */

class NotifyImpl(context: MusicService) : Notify(context) {

//  private var titleColor: Int? = null
//  private var textColor: Int? = null

//  init {
//    try {
//      val linearLayout = LayoutInflater.from(context).inflate(R.layout.notification_big, null)
//
//      val titleView = linearLayout.findViewById<TextView>(R.id.notify_song)
//      val textView = linearLayout.findViewById<TextView>(R.id.notify_artist_album)
//
//      titleColor = titleView.textColors.defaultColor
//      textColor = textView.textColors.defaultColor
//    } catch (e: Exception) {
//      LogUtils.e(e);
//    }
//  }


    override fun updateForPlaying() {
        val remoteView = RemoteViews(service.packageName, R.layout.notification)
        val remoteBigView = RemoteViews(service.packageName, R.layout.notification_big)
        val isPlay = service.isPlaying
        val song = service.currentSong
        val isSystemColor = SPUtil.getValue(service, SPUtil.SETTING_KEY.NAME, SPUtil.SETTING_KEY.NOTIFY_SYSTEM_COLOR, true)

        buildAction(service, remoteView, remoteBigView)
        val notification = buildNotification(service, remoteView, remoteBigView)

        //设置歌手，歌曲名
        remoteBigView.setTextViewText(R.id.notify_song, song.title)
        remoteBigView.setTextViewText(R.id.notify_artist_album, song.artist + " - " + song.album)

        remoteView.setTextViewText(R.id.notify_song, song.title)
        remoteView.setTextViewText(R.id.notify_artist_album, song.artist + " - " + song.album)

        //非系统背景色 即黑色背景
        if (!isSystemColor) {
            //字体颜色
            remoteBigView.setTextColor(R.id.notify_song, ColorUtil.getColor(R.color.dark_text_color_primary))
            remoteView.setTextColor(R.id.notify_song, ColorUtil.getColor(R.color.dark_text_color_primary))
            remoteBigView.setTextColor(R.id.notify_artist_album, ColorUtil.getColor(R.color.dark_text_color_secondary))
            remoteView.setTextColor(R.id.notify_artist_album, ColorUtil.getColor(R.color.dark_text_color_secondary))
            //背景
            remoteBigView.setImageViewResource(R.id.notify_bg, R.drawable.bg_notification_black)
            remoteBigView.setViewVisibility(R.id.notify_bg, View.VISIBLE)
            remoteView.setImageViewResource(R.id.notify_bg, R.drawable.bg_notification_black)
            remoteView.setViewVisibility(R.id.notify_bg, View.VISIBLE)
        }

        //桌面歌词
        remoteBigView.setImageViewResource(R.id.notify_lyric,
                if (service.isDesktopLyricLocked) R.drawable.icon_notify_desktop_lyric_unlock else R.drawable.icon_notify_lyric)

        //设置播放按钮
        if (!isPlay) {
            remoteBigView.setImageViewResource(R.id.notify_play, R.drawable.icon_notify_play)
            remoteView.setImageViewResource(R.id.notify_play, R.drawable.icon_notify_play)
        } else {
            remoteBigView.setImageViewResource(R.id.notify_play, R.drawable.icon_notify_pause)
            remoteView.setImageViewResource(R.id.notify_play, R.drawable.icon_notify_pause)
        }

        //设置封面
        val size = DensityUtil.dip2px(service, 128f)

        disposable?.dispose()
        disposable = object : RemoteUriRequest(getSearchRequestWithAlbumType(song), RequestConfig.Builder(size, size).build()) {
            override fun onStart() {
                LogUtils.v("onStart")
                remoteBigView.setImageViewResource(R.id.notify_image, R.drawable.album_empty_bg_day)
                remoteView.setImageViewResource(R.id.notify_image, R.drawable.album_empty_bg_day)
                pushNotify(notification)
            }

            override fun onError(throwable: Throwable) {
                LogUtils.v("onError")
                remoteBigView.setImageViewResource(R.id.notify_image, R.drawable.album_empty_bg_day)
                remoteView.setImageViewResource(R.id.notify_image, R.drawable.album_empty_bg_day)
                pushNotify(notification)
            }

            override fun onSuccess(result: Bitmap?) {
                LogUtils.v("onSuccess")
                try {
                    if (result != null && !result.isRecycled) {
                        remoteBigView.setImageViewBitmap(R.id.notify_image, result)
                        remoteView.setImageViewBitmap(R.id.notify_image, result)
                    } else {
                        remoteBigView.setImageViewResource(R.id.notify_image, R.drawable.album_empty_bg_day)
                        remoteView.setImageViewResource(R.id.notify_image, R.drawable.album_empty_bg_day)
                    }
                } catch (e: Exception) {
                    LogUtils.e(e)
                } finally {
                    pushNotify(notification)
                }
            }

        }.load()
    }

    private fun buildNotification(context: Context, remoteView: RemoteViews, remoteBigView: RemoteViews): Notification {
        val builder = NotificationCompat.Builder(context, PLAYING_NOTIFICATION_CHANNEL_ID)
        builder.setContent(remoteView)
                //设置大自定义通知栏
//                .setCustomBigContentView(remoteBigView)
                .setContentText("")
                .setContentTitle("")
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(service.isPlaying)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.icon_notifbar)
        //设置大自定义通知栏
//        builder.setCustomBigContentView(remoteBigView)
        builder.setCustomContentView(remoteView)
        return builder.build()
    }

    private fun buildAction(context: Context, remoteView: RemoteViews, remoteBigView: RemoteViews) {
        //添加Action
        //切换
        val playIntent = buildPendingIntent(context, Command.TOGGLE)
        remoteBigView.setOnClickPendingIntent(R.id.notify_play, playIntent)
        remoteView.setOnClickPendingIntent(R.id.notify_play, playIntent)
        //下一首
        val nextIntent = buildPendingIntent(context, Command.NEXT)
        remoteBigView.setOnClickPendingIntent(R.id.notify_next, nextIntent)
        remoteView.setOnClickPendingIntent(R.id.notify_next, nextIntent)
        //上一首
        val prevIntent = buildPendingIntent(context, Command.PREV)
        remoteBigView.setOnClickPendingIntent(R.id.notify_prev, prevIntent)

        //关闭通知栏
        val closeIntent = buildPendingIntent(context, Command.CLOSE_NOTIFY)
        remoteBigView.setOnClickPendingIntent(R.id.notify_close, closeIntent)
        remoteView.setOnClickPendingIntent(R.id.notify_close, closeIntent)

        //桌面歌词
        val lyricIntent = buildPendingIntent(context,
                if (service.isDesktopLyricLocked) Command.UNLOCK_DESKTOP_LYRIC else Command.TOGGLE_DESKTOP_LYRIC)
        remoteBigView.setOnClickPendingIntent(R.id.notify_lyric, lyricIntent)
        remoteView.setOnClickPendingIntent(R.id.notify_lyric, lyricIntent)
    }

//  private fun tintBitmap(resId: Int, color: Int?): Bitmap {
//    val bitmap = BitmapFactory.decodeResource(service.resources, resId)
//    return replaceBitmapColor(bitmap, color ?: return bitmap)
//  }
//
//  private fun replaceBitmapColor(oldBitmap: Bitmap, newColor: Int): Bitmap {
//    val copy = oldBitmap.copy(Bitmap.Config.ARGB_8888, true)
//    //循环获得bitmap所有像素点
//    val width = copy.width
//    val height = copy.height
//    for (i in 0 until height) {
//      for (j in 0 until width) {
//        val color = copy.getPixel(j, i)
//        if (color != Color.TRANSPARENT) {
//          copy.setPixel(j, i, newColor)  //替换
//        }
//      }
//    }
//    return copy
//  }

}
