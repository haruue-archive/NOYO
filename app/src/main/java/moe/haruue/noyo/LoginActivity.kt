package moe.haruue.noyo

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*

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



    }
}