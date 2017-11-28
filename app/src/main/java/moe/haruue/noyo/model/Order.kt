package moe.haruue.noyo.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.IntDef
import android.support.annotation.StringRes
import com.google.gson.annotations.SerializedName
import moe.haruue.noyo.R
import moe.haruue.noyo.utils.loge
import moe.haruue.noyo.utils.parcelableCreatorOf

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
@Suppress("MemberVisibilityCanPrivate")
data class Order(
        @SerializedName("_id") val id: String = "",
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
        const val STATUS_CANCELLED = -1

        @Suppress("unused")
        @JvmField
        val CREATOR = parcelableCreatorOf<Order>()

        @SuppressLint("SwitchIntDef")
        @StringRes fun getStringValsByStatus(@Status status: Int): Int {
            return when (status) {
                Order.STATUS_WAITING_PAY -> Order.StringVals.STATUS_WAITING_PAY
                Order.STATUS_PAID -> Order.StringVals.STATUS_PAID
                Order.STATUS_WAITING_PLANT -> Order.StringVals.STATUS_WAITING_PLANT
                Order.STATUS_PLANTED -> Order.StringVals.STATUS_PLANTED
                Order.STATUS_WAITING_HARVEST -> Order.StringVals.STATUS_WAITING_HARVEST
                Order.STATUS_HARVESTED -> Order.StringVals.STATUS_HARVESTED
                Order.STATUS_TRANSPORT -> Order.StringVals.STATUS_TRANSPORT
                Order.STATUS_DELIVERED -> Order.StringVals.STATUS_DELIVERED
                Order.STATUS_CANCELLED -> Order.StringVals.STATUS_CANCELLED
                else -> {
                    loge("Server returned unknown status: $status")
                    Order.StringVals.STATUS_WAITING_PAY
                }
            }
        }

    }


    object StringVals {
        const val STATUS_WAITING_PAY = R.string.status_waiting_pay
        const val STATUS_PAID = R.string.status_paid
        const val STATUS_WAITING_PLANT = R.string.status_waiting_plant
        const val STATUS_PLANTED = R.string.status_planted
        const val STATUS_WAITING_HARVEST = R.string.status_waiting_harvest
        const val STATUS_HARVESTED = R.string.status_harvested
        const val STATUS_TRANSPORT = R.string.status_transport
        const val STATUS_DELIVERED = R.string.status_delivered
        const val STATUS_CANCELLED = R.string.status_cancelled
    }

    @IntDef(STATUS_WAITING_PAY.toLong(),
            STATUS_PAID.toLong(),
            STATUS_WAITING_PLANT.toLong(),
            STATUS_PLANTED.toLong(),
            STATUS_WAITING_HARVEST.toLong(),
            STATUS_HARVESTED.toLong(),
            STATUS_TRANSPORT.toLong(),
            STATUS_DELIVERED.toLong(),
            STATUS_CANCELLED.toLong())
    @Retention(AnnotationRetention.SOURCE)
    annotation class Status

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