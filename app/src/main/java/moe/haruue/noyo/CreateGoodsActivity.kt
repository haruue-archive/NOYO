package moe.haruue.noyo

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_create_goods.*
import moe.haruue.noyo.api.ApiServices
import moe.haruue.noyo.model.Goods
import moe.haruue.noyo.utils.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

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

    var goods by ParcelableExtraDelegate(this, EXTRA_GOODS, Goods())
    val type by StringExtraDelegate(this, EXTRA_TYPE, TYPE_RENT_FARM)
    val action by StringExtraDelegate(this, EXTRA_ACTION, ACTION_CREATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_goods)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (action == ACTION_CREATE) {
            create.visibility = View.VISIBLE
            update.visibility = View.GONE

            supportActionBar?.title = "创建商品"
        }

        if (action == ACTION_EDIT) {
            if (goods.id.isEmpty()) {
                finish()
                return
            }

            supportActionBar?.title = "编辑商品"

            create.visibility = View.GONE
            update.visibility = View.VISIBLE

            with(goods) {
                titleEditText.setText(title)
                summaryEditText.setText(summary)
                countEditText.setText("$count")
                priceEditText.setText("$price")
                addressEditText.setText(address)
            }
        }

        supportActionBar?.subtitle = when(type) {
            TYPE_PRE_SELL -> "产品预售"
            TYPE_RENT_FARM -> "土地租赁"
            else -> throw IllegalArgumentException("Unknown type: $type")
        }

        create.setOnClickListener {

            val (te, title) = checkTextInputEmpty(titleEditText, titleLayout, "标题不能为空")
            val (se, summary) = checkTextInputEmpty(summaryEditText, summaryLayout, "详情不能为空")
            val (ce, countString) = checkTextInputEmpty(countEditText, countLayout, "数量不能为空")
            val (pe, priceString) = checkTextInputEmpty(priceEditText, priceLayout, "价格不能为空")
            val (ae, address) = checkTextInputEmpty(addressEditText, addressLayout, "产地不能为空")

            if (te || se || ce || pe || ae) {
                return@setOnClickListener
            }

            val count = countString.toIntOrNull()

            if (count == null || count <= 0) {
                countLayout.error = "数量应该是一个大于 0 的整数"
                return@setOnClickListener
            }

            val price = priceString.toDoubleOrNull()

            if (price == null || price <= 0) {
                priceLayout.error = "价格应该是一个大于 0 的数值"
                return@setOnClickListener
            }

            ApiServices.v1service.createGoods(title, summary, count, price, type, address)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createApiSubscriber {
                        onNext = {
                            toast("商品发布成功")
                            startActivity<GoodsInfoActivity> {
                                putExtra(GoodsInfoActivity.EXTRA_GOODS, it.data)
                            }
                            finish()
                        }
                    })
                    .lifecycleUnsubscribe()
        }

        update.setOnClickListener {
            val (te, title) = checkTextInputEmpty(titleEditText, titleLayout, "标题不能为空")
            val (se, summary) = checkTextInputEmpty(summaryEditText, summaryLayout, "详情不能为空")
            val (ce, countString) = checkTextInputEmpty(countEditText, countLayout, "数量不能为空")
            val (pe, priceString) = checkTextInputEmpty(priceEditText, priceLayout, "价格不能为空")
            val (ae, address) = checkTextInputEmpty(addressEditText, addressLayout, "产地不能为空")

            if (te || se || ce || pe || ae) {
                return@setOnClickListener
            }

            val count = countString.toIntOrNull()

            if (count == null || count <= 0) {
                countLayout.error = "数量应该是一个大于 0 的整数"
                return@setOnClickListener
            }

            val price = priceString.toDoubleOrNull()

            if (price == null || price <= 0) {
                priceLayout.error = "价格应该是一个大于 0 的数值"
                return@setOnClickListener
            }

            ApiServices.v1service.updateGoods(goods.id, title, summary, count, price, type, address)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createApiSubscriber {
                        onNext = {
                            toast("商品更新成功")
                            startActivity<GoodsInfoActivity> {
                                putExtra(GoodsInfoActivity.EXTRA_GOODS, it.data)
                            }
                            finish()
                        }
                    })
                    .lifecycleUnsubscribe()

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        with(outState) {
            putString("title", titleEditText.text.toString())
            putString("summary", summaryEditText.text.toString())
            putString("count", countEditText.text.toString())
            putString("price", priceEditText.text.toString())
            putString("address", addressEditText.text.toString())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        with(savedInstanceState) {
            titleEditText.setText(getString("title"))
            summaryEditText.setText(getString("summary"))
            countEditText.setText(getString("count"))
            priceEditText.setText(getString("price"))
            addressEditText.setText(getString("address"))
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