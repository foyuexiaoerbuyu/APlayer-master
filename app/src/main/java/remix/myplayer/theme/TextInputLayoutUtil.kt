package remix.myplayer.theme

import android.content.res.ColorStateList
import android.os.Build
import android.support.annotation.ColorInt
import android.support.design.widget.TextInputLayout
import remix.myplayer.util.LogUtils

/**
 * @author Aidan Follestad (afollestad)
 */
object TextInputLayoutUtil {

    fun setHint(view: TextInputLayout, @ColorInt hintColor: Int) {
        try {
            val defaultTextColorField = if (Build.VERSION.SDK_INT >= 29) {
                TextInputLayout::class.java.getDeclaredField("defaultHintTextColor")
            } else {
                TextInputLayout::class.java.getDeclaredField("mDefaultTextColor")
            }
            defaultTextColorField.isAccessible = true
            defaultTextColorField.set(view, ColorStateList.valueOf(hintColor))
        } catch (t: Throwable) {
            LogUtils.e(t)
        }

    }

    fun setAccent(view: TextInputLayout, @ColorInt accentColor: Int) {
        try {
            val focusedTextColorField = if (Build.VERSION.SDK_INT >= 29) {
                TextInputLayout::class.java.getDeclaredField("focusedTextColor")
            } else {
                TextInputLayout::class.java.getDeclaredField("mFocusedTextColor")
            }
            focusedTextColorField.isAccessible = true
            focusedTextColorField.set(view, ColorStateList.valueOf(accentColor))
        } catch (t: Throwable) {
            LogUtils.e(t)
        }

    }
}