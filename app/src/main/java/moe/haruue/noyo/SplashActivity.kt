package moe.haruue.noyo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_splash.*

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        if (App.instance.logined) {
            loginPanel.visibility = View.GONE
            Handler().postDelayed({
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }, 500)
        } else {
            loginPanel.visibility = View.VISIBLE
            loginButton.setOnClickListener {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
            registerButton.setOnClickListener {
                startActivity(Intent(this@SplashActivity, RegisterActivity::class.java))
            }
        }
    }

}