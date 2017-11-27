package moe.haruue.noyo

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_modify_password.*
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
class ModifyPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        button1.setOnClickListener {
            val (oe, originPassword) = checkTextInputEmpty(originPasswordEdit, originPasswordEditLayout, "原密码不能为空")
            val (ne, newPassword) = checkTextInputEmpty(newPasswordEdit, newPasswordEditLayout, "新密码不能为空")

            if (oe || ne) {
                return@setOnClickListener
            }

            button1.visibility = View.INVISIBLE
            progress.visibility = View.VISIBLE

            ApiServices.v1service.accountUpdate("password", newPassword, originPassword)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createApiSubscriber {
                        onComplete = {
                            toast("密码修改成功")
                            finish()
                        }
                        onApiError = {
                            when (it.errno) {
                                APIErrorList.passwordError -> { originPasswordEditLayout.error = "原密码不正确" }
                                APIErrorList.passwordTooShort,
                                APIErrorList.passwordTooLong,
                                APIErrorList.passwordWeak1,
                                APIErrorList.passwordWeak2 -> {
                                    newPasswordEditLayout.error = "密码需要满足长度为 6-32 位，至少包含大写字母、小写字母、数字、特殊符号中的任意 3 种"
                                }
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

}