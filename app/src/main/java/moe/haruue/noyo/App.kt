package moe.haruue.noyo

import android.app.Application
import moe.haruue.noyo.model.Member
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

    var member: Member = Member.INVALID_USER
        get() {
            debug {
                if (field == Member.INVALID_USER) {
                    field = Member("123456", "haruue", "Haruue Icymoon", "", "i@haruue.moe", Member.ROLE_CONSUMER, "重庆", mutableListOf())
                }
            }
            return field
        }

    inline val logined
        get() = member != Member.INVALID_USER

    override fun onCreate() {
        super.onCreate()
        instance = this@App
    }

}