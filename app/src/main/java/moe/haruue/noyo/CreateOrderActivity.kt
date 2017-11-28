package moe.haruue.noyo

import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_create_order.*
import moe.haruue.noyo.api.ApiServices
import moe.haruue.noyo.model.Goods
import moe.haruue.noyo.model.Order
import moe.haruue.noyo.utils.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

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

    val goods by ParcelableExtraDelegate(this, EXTRA_GOODS, Goods())
    val order by ParcelableExtraDelegate(this, EXTRA_ORDER, Order())
    val action by StringExtraDelegate(this, EXTRA_ACTION, ACTION_CREATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (action == ACTION_CREATE) {
            if (goods.id.isEmpty()) {
                finish()
                return
            }

            supportActionBar?.title = "创建订单"
        }

        if (action == ACTION_EDIT) {
            if (order.id.isEmpty()) {
                finish()
                return
            }

            supportActionBar?.title = "编辑订单"
        }

        create.setOnClickListener {
            val (ce, countString) = checkTextInputEmpty(countEditText, countLayout, "数量不能为空")
            val (ae, address) = checkTextInputEmpty(addressEditText, addressLayout, "地址不能为空")
            val external = externalEditText.text.toString()

            val count = countString.toIntOrNull()

            if (count == null || count <= 0) {
                countLayout.error = "数量应该是一个大于 0 的整数"
                return@setOnClickListener
            }

            ApiServices.v1service.createOrder(goods.id, count, address, external)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createApiSubscriber {
                        onNext = {
                            toast("订单创建成功")
                            startActivity<OrderInfoActivity> {
                                putExtra(OrderInfoActivity.EXTRA_ORDER, it.data)
                            }
                            finish()
                        }
                    })
                    .lifecycleUnsubscribe()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}