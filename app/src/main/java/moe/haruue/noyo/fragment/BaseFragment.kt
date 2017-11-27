package moe.haruue.noyo.fragment

import android.support.v4.app.Fragment
import rx.Subscription

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
open class BaseFragment : Fragment() {

    private val subscriptions = mutableListOf<Subscription>()

    fun Subscription.lifecycleUnsubscribe(): Subscription {
        subscriptions.add(this)
        return this
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.forEach {
            if (!it.isUnsubscribed) it.unsubscribe()
        }
    }


}