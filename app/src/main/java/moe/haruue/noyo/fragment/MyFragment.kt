package moe.haruue.noyo.fragment

import android.os.Bundle
import android.support.annotation.Keep
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_my.*
import moe.haruue.noyo.App
import moe.haruue.noyo.ModifyAccountActivity
import moe.haruue.noyo.ModifyPasswordActivity
import moe.haruue.noyo.R
import moe.haruue.noyo.api.ApiServices
import moe.haruue.noyo.api.PersistentCookieJar
import moe.haruue.noyo.model.Member
import moe.haruue.noyo.utils.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
@Keep
class MyFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_my, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modifyNicknameItemButton.setOnClickListener {
            ModifyAccountActivity.start(activity, ModifyAccountActivity.WHAT_NICKNAME)
        }
        modifyEmailItemButton.setOnClickListener {
            ModifyAccountActivity.start(activity, ModifyAccountActivity.WHAT_EMAIL)
        }
        modifyPasswordItemButton.setOnClickListener {
            activity.startActivity<ModifyPasswordActivity>()
        }
        logoutItemButton.setOnClickListener {
            logoutProgress.visibility = View.VISIBLE
            logoutItemButton.visibility = View.INVISIBLE

            ApiServices.v1service.logout()
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createApiSubscriber {
                        onComplete = {
                            activity.toast("您已安全登出")
                        }
                        onError = {
                            PersistentCookieJar.clearAllCookies()
                        }
                        onFinally = {
                            App.instance.member = Member.INVALID_USER
                            RxBus.post(Events.AuthorizationRequiredEvent())
                            logoutProgress.visibility = View.VISIBLE
                            logoutItemButton.visibility = View.INVISIBLE
                        }
                    })
                    .lifecycleUnsubscribe()
        }
    }

    override fun onResume() {
        super.onResume()
        nicknameView.text = App.instance.member.nickname
        usernameView.text = App.instance.member.username
        emailView.text = App.instance.member.email
    }

}