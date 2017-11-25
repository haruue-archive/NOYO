package moe.haruue.noyo.model

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
data class APIResultWrapper<T>(
        var status: String,
        var message: String,
        var data: T?
)