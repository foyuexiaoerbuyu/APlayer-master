package remix.myplayer.misc.log

import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import remix.myplayer.util.LogUtils

/**
 * Created by remix on 2019/1/15
 */
open class LogObserver : SingleObserver<Any> {
    private val tag = this::class.java.simpleName

    override fun onSuccess(value: Any) {
        LogUtils.v(tag, "onSuccess: %s" + value.toString())
    }

    override fun onSubscribe(d: Disposable) {
        LogUtils.v(tag, "onSubscribe")
    }

    override fun onError(e: Throwable) {
        LogUtils.v(tag, "onError: %s" + e.message)
    }
}