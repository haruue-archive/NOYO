package moe.haruue.noyo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class ModifyPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}