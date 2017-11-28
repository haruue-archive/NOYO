package moe.haruue.noyo.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import moe.haruue.noyo.utils.parcelableCreatorOf
import moe.haruue.noyo.utils.readMutableList

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
@Suppress("MemberVisibilityCanPrivate")
class Member(
        @SerializedName("_id") var id: String? = "",
        var username: String = "",
        var nickname: String = "",
        var password: String? = "",
        var email: String = "",
        province: String = DEFAULT_PROVINCE,
        var role: String = "",
        var orders: MutableList<Order> = mutableListOf()
) : Parcelable {


    @SerializedName("city")
    var province: String = province
        get() {
            return if (field in PROVINCES) {
                field
            } else {
                DEFAULT_PROVINCE
            }
        }
        set(value) {
            field = if (value in PROVINCES) {
                value
            } else {
                DEFAULT_PROVINCE
            }
        }

    @Suppress("unused")
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readMutableList())

    companion object {
        val INVALID_USER = Member("-1", "", "", "", "", "", "", mutableListOf())
        const val ROLE_CONSUMER = "consumer"
        const val ROLE_FARMER = "farmer"

        val ROLES = listOf(ROLE_CONSUMER, ROLE_FARMER)

        val DEFAULT_PROVINCE = "北京"
        val PROVINCES = arrayOf(
                "北京",
                "天津",
                "河北",
                "山西",
                "内蒙古",
                "辽宁",
                "吉林",
                "黑龙江",
                "上海",
                "江苏",
                "浙江",
                "安徽",
                "福建",
                "江西",
                "山东",
                "河南",
                "湖北",
                "湖南",
                "广东",
                "广西",
                "海南",
                "重庆",
                "四川",
                "贵州",
                "云南",
                "西藏",
                "陕西",
                "甘肃",
                "青海",
                "宁夏",
                "新疆",
                "香港",
                "澳门",
                "台湾"
        )

        @Suppress("unused")
        @JvmField
        val CREATOR = parcelableCreatorOf<Member>()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(username)
        parcel.writeString(nickname)
        parcel.writeString(password)
        parcel.writeString(email)
        parcel.writeString(province)
        parcel.writeString(role)
        parcel.writeList(orders)
    }

    override fun describeContents(): Int = 0

}