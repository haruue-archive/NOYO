@file:Suppress("unused")

package moe.haruue.noyo.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.CheckResult
import android.support.annotation.IntDef
import android.support.annotation.StringRes
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import moe.haruue.noyo.BuildConfig
import java.util.*

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

inline fun <reified T> Context.startActivity(block: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    intent.block()
    startActivity(intent)
}

inline fun <reified T> Context.startActivity() {
    startActivity<T> {}
}

inline fun <reified T> Activity.startActivityForResult(requestCode: Int, block: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.block()
    startActivityForResult(intent, requestCode)
}

inline fun <reified T> Activity.startActivityForResult(requestCode: Int) {
    startActivityForResult<T> (requestCode) {}
}

inline fun <reified T> Fragment.startActivity(block: Intent.() -> Unit) {
    val intent = Intent(this.activity, T::class.java)
    intent.block()
    startActivity(intent)
}

inline fun <reified T> Fragment.startActivity() {
    startActivity<T> {}
}

inline fun <reified T> Fragment.startActivityForResult(requestCode: Int, block: Intent.() -> Unit) {
    val intent = Intent(this.activity, T::class.java)
    intent.block()
    startActivityForResult(intent, requestCode)
}

inline fun <reified T> Fragment.startActivityForResult(requestCode: Int) {
    startActivityForResult<T> (requestCode) {}
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

@Suppress("NOTHING_TO_INLINE")
inline fun Any.logmInternal(msg: String) {
    val s = Exception().stackTrace[0]
    logd("${s.className}\$${s.methodName}: $msg}")
}

@Suppress("NOTHING_TO_INLINE")
inline fun Any.logm(vararg args: Any?) {
    logmInternal(Arrays.toString(args))
}

@Suppress("NOTHING_TO_INLINE")
inline fun Any.logr(rv: Any?) {
    val message = if (rv is Array<*>) {
        Arrays.toString(rv)
    } else {
        rv.toString()
    }
    logmInternal(message)
}

inline fun <reified T : Parcelable> parcelableCreatorOf(): Parcelable.Creator<T> = object : Parcelable.Creator<T> {
    override fun newArray(size: Int): Array<T?> = arrayOfNulls(size)

    override fun createFromParcel(source: Parcel?): T {
        return T::class.java.getDeclaredConstructor(Parcel::class.java)
                .newInstance(source)
    }
}

inline fun <reified T> Parcel.readMutableList(): MutableList<T> {
    @Suppress("UNCHECKED_CAST")
    return readArrayList(T::class.java.classLoader) as MutableList<T>
}

fun checkTextInputEmpty(edit: EditText, layout: TextInputLayout, error: String): Pair<Boolean, String> {
    val text = edit.text.toString()
    if (text.isEmpty()) {
        layout.error = error
        return true to ""
    } else {
        layout.error = null
        return false to text
    }
}

fun String?.isValidateEmail(): Boolean {
    return this?.matches(Regex("^[1-9a-zA-Z+.]+@[1-9a-zA-Z.]*[1-9a-zA-Z]+\\.[1-9a-zA-Z]+$")) ?: false
}

fun String?.isValidateUsername(): Boolean {
    return this?.matches(Regex("^[a-zA-Z_][a-zA-Z0-9_]{4,}$")) ?: false
}

@CheckResult
fun Context.confirm(title: String, message: String, r: (dialog: DialogInterface, which: Int) -> Unit): AlertDialog {
    return with(AlertDialog.Builder(this)) {
        setTitle(title)
        setMessage(message)
        setPositiveButton(android.R.string.ok, r)
        setNegativeButton(android.R.string.cancel, { _, _ -> })
        create()
    }
}