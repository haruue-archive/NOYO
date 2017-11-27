package moe.haruue.noyo

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import moe.haruue.noyo.utils.Events
import moe.haruue.noyo.utils.RxBus
import moe.haruue.noyo.utils.startActivity
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private val subscriptions = mutableListOf<Subscription>()

    fun Subscription.lifecycleUnsubscribe(): Subscription {
        subscriptions.add(this)
        return this
    }

    protected var isForeground: Boolean = false
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBus.observableOf<Events.AuthorizationRequiredEvent>()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (isForeground && this !is SplashActivity) {
                        startActivity<SplashActivity>()
                    }
                    if (this !is SplashActivity) {
                        finish()
                    }
                }
                .lifecycleUnsubscribe()
    }

    override fun onResume() {
        super.onResume()
        isForeground = true
    }

    override fun onPause() {
        super.onPause()
        isForeground = false
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.forEach {
            if (!it.isUnsubscribed) it.unsubscribe()
        }
    }

}