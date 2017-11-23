package moe.haruue.noyo.model

import android.support.annotation.StringDef
import com.google.gson.annotations.SerializedName

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
data class Goods(
        @SerializedName("_id") val id: String,
        val title: String,
        val summary: String,
        val count: Int,
        val price: Double,
        val image: String?,
        @Type val type: String,
        val seller: String,
        val orders: ArrayList<Order>,
        val address: String
) {

    companion object {
        const val TYPE_MUD = "mud"
        const val TYPE_PRODUCT = "product"
    }

    @StringDef(TYPE_MUD, TYPE_PRODUCT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Type

}