package moe.haruue.noyo

import android.os.Bundle

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class CreateGoodsActivity : BaseActivity() {

    companion object {
        const val EXTRA_GOODS = "goods"
        const val EXTRA_ACTION = "action"
        const val EXTRA_TYPE = "type"
        const val ACTION_CREATE = "create"
        const val ACTION_EDIT = "edit"
        const val TYPE_RENT_FARM = "mud"
        const val TYPE_PRE_SELL = "product"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_goods)
    }

}