package moe.haruue.noyo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IntDef
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.activity_modify_account.*

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class ModifyAccountActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_WHAT = "what"
        const val WHAT_NICKNAME = 2
        const val WHAT_EMAIL = 1

        fun start(context: Context, @What what: Int) {
            val intent = Intent(context, ModifyAccountActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            intent.putExtra(EXTRA_WHAT, what)
            context.startActivity(intent)
        }
    }

    @What val what by lazy { intent?.getIntExtra(EXTRA_WHAT, WHAT_NICKNAME) ?: WHAT_NICKNAME }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_account)
        when (what) {
            WHAT_NICKNAME -> {
                supportActionBar?.setTitle(R.string.modify_nickname)
                edit.setHint(R.string.nick)
                text1.hint = getString(R.string.nick)
                edit.inputType = EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            }
            WHAT_EMAIL -> {
                supportActionBar?.setTitle(R.string.modify_email)
                edit.setHint(R.string.email)
                text1.hint = getString(R.string.email)
                edit.inputType = EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @IntDef(WHAT_NICKNAME.toLong(),
            WHAT_EMAIL.toLong())
    @Retention(AnnotationRetention.SOURCE)
    annotation class What
}