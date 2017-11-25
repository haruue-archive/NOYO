package moe.haruue.noyo

import android.os.Bundle

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class ModifyPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}