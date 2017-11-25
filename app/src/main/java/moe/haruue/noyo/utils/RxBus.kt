package moe.haruue.noyo.utils

import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
object RxBus {

    val bus = SerializedSubject<Any, Any>(PublishSubject.create())

    fun post(o: Any) {
        bus.onNext(o)
    }

    inline fun <reified T> observableOf() = bus.ofType(T::class.java)
}

class Events {
    class AuthorizationRequiredEvent
}
