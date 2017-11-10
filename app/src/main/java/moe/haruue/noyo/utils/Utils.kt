@file:Suppress("unused")

package moe.haruue.noyo.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.annotation.IntDef
import android.support.annotation.StringRes
import android.util.Log
import android.widget.Toast
import moe.haruue.noyo.BuildConfig

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

@IntDef(Toast.LENGTH_SHORT.toLong(), Toast.LENGTH_LONG.toLong())
@Retention(AnnotationRetention.SOURCE)
annotation class Duration

fun Context.toast(msg: String, @Duration duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(applicationContext, msg, duration).show()
}

fun Context.toast(@StringRes resId: Int, @Duration duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(applicationContext, resId, duration).show()
}

inline fun <reified T> Context.startActivity() {
    val intent = Intent(this, T::class.java)
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

inline fun <reified T> Activity.startActivityForResult(requestCode: Int) {
    val intent = Intent(this, T::class.java)
    startActivityForResult(intent, requestCode)
}

inline fun debug(r: () -> Unit) {
    return when {
        BuildConfig.DEBUG -> r()
        else -> {}
    }
}

private inline fun internalLog(nt: (tag: String, msg: String) -> Int, wt: (tag: String, msg: String, tr: Throwable) -> Int, tag: String, msg: String, tr: Throwable?): Int {
    debug {
        return if (tr == null) {
            nt(tag, msg)
        } else {
            wt(tag, msg, tr)
        }
    }
    return -1
}

fun Any.logv(msg: String, e: Throwable? = null) = internalLog(Log::v, Log::v, this.javaClass.simpleName, msg, e)

fun Any.logi(msg: String, e: Throwable? = null) = internalLog(Log::i, Log::i, this.javaClass.simpleName, msg, e)

fun Any.logd(msg: String, e: Throwable? = null) = internalLog(Log::d, Log::d, this.javaClass.simpleName, msg, e)

fun Any.logw(msg: String, e: Throwable? = null) = internalLog(Log::w, Log::w, this.javaClass.simpleName, msg, e)

fun Any.loge(msg: String, e: Throwable? = null) = internalLog(Log::e, Log::e, this.javaClass.simpleName, msg, e)

fun Any.logwtf(msg: String, e: Throwable? = null) = internalLog(Log::wtf, Log::wtf, this.javaClass.simpleName, msg, e)
