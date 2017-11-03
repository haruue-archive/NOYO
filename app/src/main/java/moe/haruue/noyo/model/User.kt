package moe.haruue.noyo.model

import com.google.gson.annotations.SerializedName

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
data class User(
        @SerializedName("_id") val id: String,
        val username: String,
        val password: String?,
        val email: String,
        val nickname: String,
        val role: String
) {
    companion object {
        const val ROLE_CONSUMER = "consumer"
        const val ROLE_FARMER = "farmer"
    }
}