package moe.haruue.noyo.fragment

import android.os.Bundle
import android.support.annotation.Keep
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_my.*
import moe.haruue.noyo.App
import moe.haruue.noyo.ModifyAccountActivity
import moe.haruue.noyo.ModifyPasswordActivity
import moe.haruue.noyo.R
import moe.haruue.noyo.utils.startActivity

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
@Keep
class MyFragment : Fragment() {
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

        }
    }

    override fun onResume() {
        super.onResume()
        nicknameView.text = App.instance.member.nickname
        usernameView.text = App.instance.member.username
        emailView.text = App.instance.member.email
    }

}