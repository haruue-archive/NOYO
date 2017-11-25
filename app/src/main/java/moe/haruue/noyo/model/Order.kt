package moe.haruue.noyo.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import moe.haruue.noyo.utils.parcelableCreatorOf

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
@Suppress("MemberVisibilityCanPrivate")
data class Order(
        @SerializedName("_id") val id: String,
        var goodsId: String = "",
        var title: String = "",
        var summary: String = "",
        var count: Int = 0,
        var price: Double = 0.0,
        var image: String? = "",
        @Goods.Type var type: String = "",
        var seller: String = "",
        var buyer: String = "",
        var status: Int = STATUS_WAITING_PAY,
        var address: String = "",
        var external: String = ""
) : Parcelable {
    @Suppress("unused")
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString())

    companion object {
        const val STATUS_WAITING_PAY = 0
        const val STATUS_PAID = 1
        const val STATUS_WAITING_PLANT = 2
        const val STATUS_PLANTED = 3
        const val STATUS_WAITING_HARVEST = 4
        const val STATUS_HARVESTED = 5
        const val STATUS_TRANSPORT = 6
        const val STATUS_DELIVERED = 7

        @Suppress("unused")
        val CREATOR = parcelableCreatorOf<Order>()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(goodsId)
        parcel.writeString(title)
        parcel.writeString(summary)
        parcel.writeInt(count)
        parcel.writeDouble(price)
        parcel.writeString(image)
        parcel.writeString(type)
        parcel.writeString(seller)
        parcel.writeString(buyer)
        parcel.writeInt(status)
        parcel.writeString(address)
        parcel.writeString(external)
    }

    override fun describeContents(): Int = 0

}