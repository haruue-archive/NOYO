package moe.haruue.noyo

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import moe.haruue.noyo.api.ApiServices
import moe.haruue.noyo.model.APIErrorList
import moe.haruue.noyo.utils.checkTextInputEmpty
import moe.haruue.noyo.utils.createApiSubscriber
import moe.haruue.noyo.utils.toast
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class LoginActivity : BaseActivity() {

    companion object {
        const val EXTRA_USERNAME = "username"
        const val EXTRA_PASSWORD = "password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extraUsername = intent?.getStringExtra(EXTRA_USERNAME)
        val extraPassword = intent?.getStringExtra(EXTRA_PASSWORD)

        if (!extraUsername.isNullOrEmpty()) {
            usernameEditText.setText(extraUsername)
            if (!extraPassword.isNullOrEmpty()) {
                passwordEditText.setText(extraPassword)
            }
        }

        button1.setOnClickListener {
            val (ue, username) = checkTextInputEmpty(usernameEditText, usernameLayout, "用户名不能为空")
            val (pe, password) = checkTextInputEmpty(passwordEditText, passwordLayout, "密码不能为空")

            if (ue || pe) return@setOnClickListener

            button1.visibility = View.INVISIBLE
            progress.visibility = View.VISIBLE

            ApiServices.v1service.login(username, password, 1)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createApiSubscriber {
                        onNext = {
                            toast("登录成功")
                            App.instance.member = it.data!!
                            // back to SplashActivity and it will delegate us to MainActivity
                            onBackPressed()
                        }
                        onComplete = {
                            finish()
                        }
                        onApiError = {
                            when (it.errno) {
                                APIErrorList.usernameEmpty,
                                APIErrorList.passwordEmpty -> { toast("所有字段都是必填字段") }
                                APIErrorList.accountNotExist -> { usernameLayout.error = "用户不存在" }
                                APIErrorList.accountDisabled -> { usernameLayout.error = "根据相关法律法规，该账户已被停用" }
                                APIErrorList.passwordError -> { passwordLayout.error = "密码错误" }
                            }
                        }
                        onFinally = {
                            button1.visibility = View.VISIBLE
                            progress.visibility = View.INVISIBLE
                        }
                    })
                    .lifecycleUnsubscribe()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        with(outState) {
            putString("username", usernameEditText.text.toString())
            putString("password", passwordEditText.text.toString())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        with(savedInstanceState) {
            usernameEditText.setText(getString("username"))
            passwordEditText.setText(getString("password"))
        }
    }

}