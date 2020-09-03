package remix.myplayer.misc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import remix.myplayer.misc.manager.ActivityManager;
import remix.myplayer.misc.manager.ServiceManager;
import remix.myplayer.util.LogUtils;

import static remix.myplayer.service.MusicService.TAG_LIFECYCLE;

/**
 * Created by taeja on 16-2-16.
 */

/**
 * 接受程序退出的广播
 */
public class ExitReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.v("开始关闭app");
        //停止所有service
        ServiceManager.StopAll();
//      //关闭通知
//        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
        //停止摇一摇
//        ShakeDetector.getInstance().stopListen();
        //关闭所有activity
        ActivityManager.FinishAll();
        new Handler().postDelayed(() -> {
            LogUtils.v(TAG_LIFECYCLE, "关闭App");
            System.exit(0);
        }, 1000);
//        System.exit(0);
    }
}
