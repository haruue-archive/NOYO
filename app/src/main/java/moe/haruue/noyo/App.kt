package moe.haruue.noyo

import android.app.Application
import moe.haruue.noyo.model.User

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    var user: User? = null

    val logined = user != null

    override fun onCreate() {
        super.onCreate()
        instance = this@App
    }

}