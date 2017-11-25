package moe.haruue.noyo

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*
import moe.haruue.noyo.api.ApiServices
import moe.haruue.noyo.model.APIErrorList
import moe.haruue.noyo.model.Member
import moe.haruue.noyo.utils.*
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

class RegisterActivity : BaseActivity() {

    var apiRequestSubscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var role = Member.ROLE_CONSUMER
        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                role = Member.ROLES[position]
            }
        }

        button1.setOnClickListener {
            val (ue, username) = checkTextInputEmpty(usernameEditText, usernameLayout, "用户名不能为空")
            val (pe, password) = checkTextInputEmpty(passwordEditText, passwordLayout, "密码不能为空")
            val (ne, nickname) = checkTextInputEmpty(nickEditText, nickLayout, "昵称不能为空")
            val (ee, email) = checkTextInputEmpty(emailEditText, emailLayout, "邮箱不能为空")

            if (ue || pe || ne || ee) return@setOnClickListener

            if (!email.isValidateEmail()) {
                emailLayout.error = "邮箱格式不正确"
                return@setOnClickListener
            } else {
                emailLayout.error = null
            }

            button1.visibility = View.INVISIBLE
            progress.visibility = View.VISIBLE

            apiRequestSubscription = ApiServices.v1service.register(email, username, nickname, password, role)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createApiSubscriber {
                        onNext = {
                            toast("注册成功")
                            startActivity<LoginActivity> {
                                putExtra(LoginActivity.EXTRA_USERNAME, it.data?.username)
                                putExtra(LoginActivity.EXTRA_PASSWORD, password)
                            }
                        }
                        onComplete = {
                            finish()
                        }
                        onApiError = {
                            when (it.errno) {
                                APIErrorList.informationNotComplete -> { toast("所有字段都是必填字段", Toast.LENGTH_LONG) }
                                APIErrorList.emailMalformed -> { emailLayout.error = "邮箱格式不正确" }
                                APIErrorList.emailUsed -> { emailLayout.error = "此邮箱已被注册，请尝试直接登录或者找回密码" }
                                APIErrorList.usernameUsed -> { usernameLayout.error = "用户名已被占用，请尝试使用其它用户名注册" }
                                APIErrorList.passwordTooShort,
                                APIErrorList.passwordTooLong,
                                APIErrorList.passwordWeak1,
                                APIErrorList.passwordWeak2 -> {
                                    passwordLayout.error = "密码需要满足长度为 6-32 位，至少包含大写字母、小写字母、数字、特殊符号中的任意 3 种"
                                }
                            }
                        }
                        onFinally = {
                            button1.visibility = View.VISIBLE
                            progress.visibility = View.INVISIBLE
                        }
                    })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        with(outState) {
            putString("username", usernameEditText.text.toString())
            putString("password", passwordEditText.text.toString())
            putString("email", emailEditText.text.toString())
            putString("nickname", nickEditText.text.toString())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        with(savedInstanceState) {
            usernameEditText.setText(getString("username"))
            passwordEditText.setText(getString("password"))
            emailEditText.setText(getString("email"))
            nickEditText.setText(getString("nickname"))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        apiRequestSubscription?.unsubscribe()
    }

}