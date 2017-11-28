package moe.haruue.noyo.utils

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.widget.TextView
import moe.haruue.noyo.R
import kotlin.reflect.KProperty

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class TextViewStringDelegate(private val textView: TextView) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return textView.text.toString()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        textView.text = value
    }
}

class TextViewPriceDelegate(private val textView: TextView) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Double {
        return textView.text.toString().removePrefix("￥").toDouble()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
        textView.text = textView.context.getString(R.string.format_price, value)
    }
}

class StringExtraDelegate(val activity: Activity, val key: String, val defaultValue: String) {

    private var field: String? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        logd("${activity.intent?.toUri(Intent.URI_ALLOW_UNSAFE)}")
        if (field == null) {
            field = activity.intent?.getStringExtra(key) ?: defaultValue
        }
        return field ?: defaultValue
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        logm(value)
        field = value
    }
}

class ParcelableExtraDelegate<T : Parcelable>(val activity: Activity, val key: String, val defaultValue: T) {

    private var field: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (field == null) {
            field = activity.intent?.getParcelableExtra(key) ?: defaultValue
        }
        return field ?: defaultValue
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        field = value
    }
}

