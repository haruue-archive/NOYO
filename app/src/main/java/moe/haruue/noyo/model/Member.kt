package moe.haruue.noyo.model

import com.google.gson.annotations.SerializedName

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class Member(
        @SerializedName("_id") var id: String?,
        var username: String,
        var nickname: String,
        var password: String?,
        var email: String,
        val city: String,
        val role: String,
        province: String
) {

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

    fun sync(preferUpload: Boolean) {
        // TODO: implement it to upload or download userdata
    }

    companion object {
        val INVALID_USER = Member("-1", "", "", "", "", "", "", "")
        const val ROLE_CONSUMER = "consumer"
        const val ROLE_FARMER = "farmer"
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
    }

}