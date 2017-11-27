package moe.haruue.noyo

import android.os.Bundle

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class CreateOrderActivity : BaseActivity() {

    companion object {
        const val EXTRA_GOODS = "goods"
        const val EXTRA_ORDER = "order"
        const val EXTRA_ACTION = "action"
        const val ACTION_CREATE = "create"
        const val ACTION_EDIT = "edit"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)
    }

}