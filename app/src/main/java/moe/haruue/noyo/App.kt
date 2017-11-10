package moe.haruue.noyo

import android.app.Application
import moe.haruue.noyo.model.User
import moe.haruue.noyo.utils.debug

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    var user: User = User.INVALID_USER
        get() {
            debug {
                if (field == User.INVALID_USER) {
                    field = User("123456", "haruue", "", "i@haruue.moe", "Haruue Icymoon", User.ROLE_CONSUMER, "重庆")
                }
            }
            return field
        }

    inline val logined
        get() = user != User.INVALID_USER

    override fun onCreate() {
        super.onCreate()
        instance = this@App
    }

}