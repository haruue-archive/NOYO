package moe.haruue.noyo.model

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.StringDef
import com.google.gson.annotations.SerializedName
import moe.haruue.noyo.utils.parcelableCreatorOf
import moe.haruue.noyo.utils.readMutableList

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
@Suppress("MemberVisibilityCanPrivate")
data class Goods(
        @SerializedName("_id") val id: String = "",
        val title: String = "",
        val summary: String = "",
        val count: Int = 0,
        val price: Double = 0.0,
        val image: String? = "",
        @Type val type: String = "",
        val seller: String = "",
        val orders: MutableList<Order> = mutableListOf(),
        val address: String
) : Parcelable {

    @Suppress("unused")
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readMutableList(),
            parcel.readString())

    companion object {
        const val TYPE_MUD = "mud"
        const val TYPE_PRODUCT = "product"

        @Suppress("unused")
        val CREATOR = parcelableCreatorOf<Goods>()
    }

    @StringDef(TYPE_MUD, TYPE_PRODUCT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Type

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(summary)
        parcel.writeInt(count)
        parcel.writeDouble(price)
        parcel.writeString(image)
        parcel.writeString(type)
        parcel.writeString(seller)
        parcel.writeList(orders)
        parcel.writeString(address)
    }

    override fun describeContents(): Int = 0

}