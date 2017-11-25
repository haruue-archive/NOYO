package moe.haruue.noyo.model

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
data class ApiResultWrapper<T>(
        val status: String,
        val message: String,
        val data: T?
)