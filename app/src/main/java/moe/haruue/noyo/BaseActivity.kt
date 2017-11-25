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

    var authorizationRequiredEventObserver: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authorizationRequiredEventObserver = RxBus.observableOf<Events.AuthorizationRequiredEvent>()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    startActivity<SplashActivity>()
                    finish()
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        authorizationRequiredEventObserver?.unsubscribe()
    }

}