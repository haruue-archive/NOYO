package moe.haruue.noyo.utils

import android.widget.Toast
import com.google.gson.Gson
import moe.haruue.noyo.App
import moe.haruue.noyo.model.APIError
import moe.haruue.noyo.model.APIErrorList
import moe.haruue.noyo.model.Member
import retrofit2.HttpException
import rx.Subscriber
import java.io.IOException

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
open class ApiSubscriber<T>(
        private val where: String,
        private val onApiErrorCallback: ((e: APIError) -> Unit)?,
        private val onNetworkErrorCallback: ((e: IOException) -> Unit)?,
        private val onOtherErrorCallback: ((e: Throwable) -> Unit)?,
        private val onErrorCallback: ((e: Throwable) -> Unit)?,
        private val onNextCallback: ((t: T) -> Unit)?,
        private val onCompleteCallback: (() -> Unit)?,
        private val onFinallyCallback: (() -> Unit)?,
        private val onStartCallback: (() -> Unit)?
) : Subscriber<T>() {

    @Suppress("MemberVisibilityCanPrivate")
    class Builder<T> {
        var where: String = ""
        var onApiError: ((e: APIError) -> Unit)? = null
        var onNetworkError: ((e: IOException) -> Unit)? = null
        var onOtherError: ((e: Throwable) -> Unit)? = null
        var onError: ((e: Throwable) -> Unit)? = null
        var onNext: ((t: T) -> Unit)? = null
        var onComplete: (() -> Unit)? = null
        var onFinally: (() -> Unit)? = null
        var onStart: (() -> Unit)? = null

        fun build(): ApiSubscriber<T> {
            return ApiSubscriber(where, onApiError, onNetworkError, onOtherError, onError, onNext, onComplete, onFinally, onStart)
        }
    }

    override fun onStart() {
        onStartCallback?.invoke()
    }

    open fun onApiError(e: APIError) {
        onApiErrorCallback?.invoke(e) ?: debug {
            App.instance.toast("API: $e", Toast.LENGTH_LONG)
        }
    }

    open fun onNetworkError(e: IOException) {
        debug {
            App.instance.toast("Network: $e", Toast.LENGTH_LONG)
        }
        onNetworkErrorCallback?.invoke(e) ?: App.instance.toast("网络暂时不可用")
    }

    override fun onError(e: Throwable) {
        logw("$where#onError", e)
        onErrorCallback?.invoke(e) ?:
        when(e) {
            is HttpException -> {
                logw("$where#onHttpError", e)
                val err = e.readApiError()
                logw("$where#onApiError: $err")
                if (err.code == 401) {
                    App.instance.toast("身份信息过期，请重新登录")
                    App.instance.member = Member.INVALID_USER
                    RxBus.post(Events.AuthorizationRequiredEvent())
                    return
                }
                if (err.code == 500 && err.errno == APIErrorList.unexpectedDatabaseError) {
                    App.instance.toast("发生未知错误，请与客户服务联系", Toast.LENGTH_LONG)
                    return
                }
                if (err.code == 403) {
                    App.instance.toast("您当前登录的账户的权限不足以进行此操作", Toast.LENGTH_LONG)
                    return
                }
                onApiError(err)
            }
            is IOException -> {
                logw("$where#onNetworkError", e)
                onNetworkError(e)
            }
            else -> {
                onOtherErrorCallback?.invoke(e)
            }
        }
        onFinallyCallback?.invoke()
    }

    override fun onCompleted() {
        onCompleteCallback?.invoke()
        onFinallyCallback?.invoke()
    }

    override fun onNext(t: T) {
        onNextCallback?.invoke(t)
    }

    private fun HttpException.readApiError(): APIError {
        val err = gson.fromJson(this.response().errorBody()!!.string(), APIError::class.java)
        err.code = this.code()
        return err
    }

    companion object {
        private val gson = Gson()
    }
}

inline fun <reified T, reified W> W.createApiSubscriber(block: ApiSubscriber.Builder<T>.() -> Unit): ApiSubscriber<T> {
    val builder = ApiSubscriber.Builder<T>()
    builder.where = W::class.java.simpleName
    builder.block()
    return builder.build()
}