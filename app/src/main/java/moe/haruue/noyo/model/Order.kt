package moe.haruue.noyo.model

import com.google.gson.annotations.SerializedName

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
data class Order(
        @SerializedName("_id") val id: String,
        val goodsId: String,
        val title: String,
        val summary: String,
        val count: Int,
        val price: Double,
        val image: String?,
        @Goods.Type val type: String,
        val seller: String,
        val buyer: String,
        val status: Int,
        val address: String,
        val external: String
) {
    companion object {
        const val STATUS_WAITING_PAY = 0
        const val STATUS_PAID = 1
        const val STATUS_WAITING_PLANT = 2
        const val STATUS_PLANTED = 3
        const val STATUS_WAITING_HARVEST = 4
        const val STATUS_HARVESTED = 5
        const val STATUS_TRANSPORT = 6
        const val STATUS_DELIVERED = 7
    }
}