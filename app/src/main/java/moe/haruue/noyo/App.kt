package moe.haruue.noyo

import android.app.Application
import com.google.gson.Gson
import moe.haruue.noyo.model.Member

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class App : Application() {

    companion object {
        lateinit var instance: App
            private set

        private val gson = Gson()
    }

    var member: Member = Member.INVALID_USER
        get() {
            if (field == Member.INVALID_USER) {
                val json = getSharedPreferences("account", MODE_PRIVATE).getString("member", null)
                var member: Member? = null
                if (!json.isNullOrEmpty()) {
                    member = gson.fromJson<Member>(json, Member::class.java)
                }
                field = member ?: Member.INVALID_USER
            }
            return field
        }
        set(value) {
            val editor = getSharedPreferences("account", MODE_PRIVATE).edit()
            if (value == Member.INVALID_USER) {
                editor.remove("member")
            } else {
                val json = gson.toJson(value)
                editor.putString("member", json)
            }
            editor.apply()
            field = value
        }

    inline val logined
        get() = member != Member.INVALID_USER

    override fun onCreate() {
        super.onCreate()
        instance = this@App
    }

}