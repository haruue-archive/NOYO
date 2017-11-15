package moe.haruue.noyo.utils

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
