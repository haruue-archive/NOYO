package moe.haruue.noyo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_order_info.*
import moe.haruue.noyo.api.ApiServices
import moe.haruue.noyo.model.Member
import moe.haruue.noyo.model.Order
import moe.haruue.noyo.utils.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class OrderInfoActivity : BaseActivity() {

    companion object {
        const val EXTRA_ORDER = "order"
    }

    var order by ParcelableExtraDelegate(this, EXTRA_ORDER, Order())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_info)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        logm(order)

        if (order.id.isEmpty()) {
            finish()
            return
        }

        when(App.instance.member.role) {
            Member.ROLE_CONSUMER -> {
                consumerPanel.visibility = View.VISIBLE
                sellerPanel.visibility = View.GONE
                noPermissionTip.visibility = View.GONE
            }
            Member.ROLE_FARMER -> {
                if (order.seller == App.instance.member.id) {
                    consumerPanel.visibility = View.GONE
                    sellerPanel.visibility = View.VISIBLE
                    noPermissionTip.visibility = View.GONE
                } else {
                    consumerPanel.visibility = View.GONE
                    sellerPanel.visibility = View.VISIBLE
                    noPermissionTip.visibility = View.GONE
                }
            }
        }

        contact.setOnClickListener {

        }

        cancel.setOnClickListener {
            confirm("取消订单", "此操作不可撤销，确定要取消此订单吗？") { _, _ ->
                ApiServices.v1service.cancelOrder(order.id)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(createApiSubscriber {
                            onNext = {
                                toast("取消订单成功")
                                finish()
                            }
                        })
                        .lifecycleUnsubscribe()
            }.show()
        }

        progress.setOnRefreshListener { refresh() }

        onReloadData()
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    @SuppressLint("SetTextI18n")
    fun onReloadData() {
        supportActionBar?.title = "订单详情 - ${order.title}"

        if (!order.image.isNullOrEmpty()) {
            @Suppress("DEPRECATION")
            Glide.with(this)
                    .load(order.image).into(imageView)
                    .onLoadFailed(resources.getDrawable(R.drawable.ph_no_pic))
        }

        titleView.text = order.title
        summaryView.text = order.summary
        addressView.text = order.address
        countView.text = "${order.count} 件"
        externalView.text = order.external
        statusView.text = getText(Order.getStringValsByStatus(order.status))
    }

    fun refresh() {
        ApiServices.v1service.infoOrder(order.id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createApiSubscriber {
                    onStart = {
                        progress.isRefreshing = true
                    }
                    onNext = {
                        order = it.data ?: order
                    }
                    onComplete = {
                        onReloadData()
                    }
                    onFinally = {
                        progress.isRefreshing = false
                    }
                })
                .lifecycleUnsubscribe()
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