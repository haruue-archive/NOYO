package moe.haruue.noyo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import moe.haruue.noyo.utils.logd
import moe.haruue.noyo.utils.startActivity
import moe.haruue.noyo.utils.startActivityForResult
import moe.haruue.noyo.utils.toast

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