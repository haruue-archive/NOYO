package moe.haruue.noyo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import moe.haruue.noyo.api.ApiServices
import moe.haruue.noyo.utils.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class MainActivity : BaseActivity() {

    companion object {
        const val REQUEST_PROVINCE = 0x51
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!App.instance.logined) {
            toast(R.string.tip_un_login)
            startActivity<SplashActivity>()
            finish()
            return
        }
        onReloadProvince()
        provinceTextView.setOnClickListener {
            startActivityForResult<ProvinceChooserActivity>(REQUEST_PROVINCE)
        }
        ApiServices.v1service.accountInfo()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createApiSubscriber {
                    onNext = {
                        App.instance.member = it.data ?: App.instance.member
                    }
                    onApiError = {
                        toast("同步用户信息失败")
                    }
                })
    }

    fun onReloadProvince() {
        logd("onReloadProvince(): App.instance.user.province=${App.instance.member.province}")
        provinceTextView.text = App.instance.member.province
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_PROVINCE -> if (resultCode == Activity.RESULT_OK) { onReloadProvince() }
        }
    }


}