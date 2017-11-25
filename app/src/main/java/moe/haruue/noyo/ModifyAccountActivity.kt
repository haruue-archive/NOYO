package moe.haruue.noyo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IntDef
import android.view.View
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.activity_modify_account.*
import moe.haruue.noyo.api.ApiServices
import moe.haruue.noyo.model.APIErrorList
import moe.haruue.noyo.utils.createApiSubscriber
import moe.haruue.noyo.utils.isValidateEmail
import moe.haruue.noyo.utils.toast
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class ModifyAccountActivity : BaseActivity() {

    companion object {
        const val EXTRA_WHAT = "what"
        const val WHAT_NICKNAME = 2
        const val WHAT_EMAIL = 1

        fun start(context: Context, @What what: Int) {
            val intent = Intent(context, ModifyAccountActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            intent.putExtra(EXTRA_WHAT, what)
            context.startActivity(intent)
        }
    }

    @What val what by lazy { intent?.getIntExtra(EXTRA_WHAT, WHAT_NICKNAME) ?: WHAT_NICKNAME }

    private var apiAccountUpdateSubscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_account)
        when (what) {
            WHAT_NICKNAME -> {
                supportActionBar?.setTitle(R.string.modify_nickname)
                edit.setHint(R.string.nick)
                text1.hint = getString(R.string.nick)
                edit.inputType = EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            }
            WHAT_EMAIL -> {
                supportActionBar?.setTitle(R.string.modify_email)
                edit.setHint(R.string.email)
                text1.hint = getString(R.string.email)
                edit.inputType = EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        button1.setOnClickListener {
            val paramWhat = when (what) {
                WHAT_EMAIL -> "email"
                WHAT_NICKNAME -> "nickname"
                else -> "nickname"
            }

            val paramValue = edit.text.toString()

            if (what == WHAT_EMAIL) {
                if (paramValue.isEmpty()) {
                    text1.error = "邮箱不能为空"
                }
                if (!paramValue.isValidateEmail()) {
                    text1.error = "邮箱格式错误"
                }
            }

            button1.visibility = View.INVISIBLE
            progress.visibility = View.VISIBLE

            apiAccountUpdateSubscription = ApiServices.v1service.accountUpdate(paramWhat, paramValue)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createApiSubscriber {
                        onNext = {
                            App.instance.member = it.data ?: App.instance.member
                        }
                        onApiError = {
                            when (it.errno) {
                                APIErrorList.emailMalformed -> { text1.error = "邮箱格式错误" }
                                APIErrorList.emailUsed -> { text1.error = "此邮箱已被注册" }
                            }
                        }
                        onComplete = {
                            toast("修改成功")
                            finish()
                        }
                        onFinally = {
                            button1.visibility = View.VISIBLE
                            progress.visibility = View.INVISIBLE
                        }
                    })


        }

    }

    override fun onDestroy() {
        super.onDestroy()
        apiAccountUpdateSubscription?.unsubscribe()
    }

    @IntDef(WHAT_NICKNAME.toLong(),
            WHAT_EMAIL.toLong())
    @Retention(AnnotationRetention.SOURCE)
    annotation class What
}