package moe.haruue.noyo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}