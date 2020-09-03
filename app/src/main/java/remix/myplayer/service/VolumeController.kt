package remix.myplayer.service

import android.os.CountDownTimer
import android.os.Handler
import android.support.annotation.FloatRange
import remix.myplayer.util.LogUtils;
import java.lang.ref.WeakReference

/**
 * Created by Remix on 2018/3/13.
 */

class VolumeController(musicService: MusicService) {
    private val mRef = WeakReference<MusicService>(musicService)
    private val mHandler: Handler = Handler()
    private val mFadeInRunnable: Runnable = Runnable {
        val mediaPlayer = mRef.get()?.mediaPlayer
        object : CountDownTimer(DURATION_IN_MS, DURATION_IN_MS / 10) {
            override fun onFinish() {
                directTo(1f)
            }

            override fun onTick(millisUntilFinished: Long) {
                val volume = 1f - millisUntilFinished * 1.0f / DURATION_IN_MS
                try {
                    mediaPlayer?.setVolume(volume, volume)
                } catch (e: IllegalStateException) {
                    LogUtils.e(e)
                }
            }
        }.start()
    }
    private val mFadeOutRunnable: Runnable = Runnable {
        val mediaPlayer = mRef.get()?.mediaPlayer
        object : CountDownTimer(DURATION_IN_MS, DURATION_IN_MS / 10) {
            override fun onTick(millisUntilFinished: Long) {
                val volume = millisUntilFinished * 1.0f / DURATION_IN_MS
                try {
                    mediaPlayer?.setVolume(volume, volume)
                } catch (e: IllegalStateException) {
                    LogUtils.e(e)
                }
            }

            override fun onFinish() {
                directTo(0f)
                try {
                    mediaPlayer?.pause()
                } catch (e: IllegalStateException) {
                    LogUtils.e(e)
                }
            }

        }.start()
    }

    fun directTo(@FloatRange(from = 0.0, to = 1.0) toVolume: Float) {
        directTo(toVolume, toVolume)
    }

    private fun directTo(@FloatRange(from = 0.0, to = 1.0) leftVolume: Float, @FloatRange(from = 0.0, to = 1.0) rightVolume: Float) {
        val mediaPlayer = mRef.get()?.mediaPlayer
        try {
            mediaPlayer?.setVolume(leftVolume, rightVolume)
        } catch (e: IllegalStateException) {
            LogUtils.e(e)
        }
    }

    /**
     * 淡入
     */
    fun fadeIn() {
        mHandler.removeCallbacks(mFadeInRunnable)
        mHandler.removeCallbacks(mFadeOutRunnable)
        mHandler.post(mFadeInRunnable)
    }

    /**
     * 淡出
     */
    fun fadeOut() {
        mHandler.removeCallbacks(mFadeInRunnable)
        mHandler.removeCallbacks(mFadeOutRunnable)
        mHandler.post(mFadeOutRunnable)
    }

    companion object {
        private const val DURATION_IN_MS = 600L
    }
}