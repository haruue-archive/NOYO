package moe.haruue.noyo.fragment

import android.os.Bundle
import android.support.annotation.Keep
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_order.*
import moe.haruue.noyo.App
import moe.haruue.noyo.OrderInfoActivity
import moe.haruue.noyo.R
import moe.haruue.noyo.api.ApiServices
import moe.haruue.noyo.model.Order
import moe.haruue.noyo.utils.TextViewPriceDelegate
import moe.haruue.noyo.utils.TextViewStringDelegate
import moe.haruue.noyo.utils.createApiSubscriber
import moe.haruue.noyo.utils.startActivity
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
@Keep
class OrderFragment : BaseFragment() {

    private val adapter = OrderAdapter(this::onEmpty, this::onLoaded) {
        startActivity<OrderInfoActivity> {
            putExtra(OrderInfoActivity.EXTRA_ORDER, it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        error.setOnClickListener { refresh() }
        progress.setOnRefreshListener { refresh() }
        list.layoutManager = LinearLayoutManager(activity)
        list.adapter = adapter
        adapter.data.clear()
        adapter.data.addAll(App.instance.member.orders)
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        onLoading()
        refresh()
    }

    fun refresh() {
        onLoading()
        ApiServices.v1service.listOrder()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createApiSubscriber {
                    onStart = {
                        onLoading()
                    }
                    onNext = {
                        adapter.data.clear()
                        val data = it.data ?: App.instance.member.orders
                        if (data.isEmpty()) {
                            onEmpty()
                        }
                        adapter.data.addAll(it.data ?: App.instance.member.orders)
                        adapter.notifyDataSetChanged()
                    }
                    onNetworkError = { onError() }
                    onOtherError = { onError() }
                    onFinally = {
                        progress.isRefreshing = false
                    }
                })
                .lifecycleUnsubscribe()
    }

    fun onEmpty() {
        progress.isRefreshing = false
        error.visibility = View.GONE
        empty.visibility = View.VISIBLE
    }

    fun onLoaded() {
        progress.isRefreshing = false
        error.visibility = View.GONE
        empty.visibility = View.GONE
    }

    fun onLoading() {
        progress.isRefreshing = true
        error.visibility = View.GONE
        empty.visibility = View.GONE
    }

    fun onError() {
        progress.isRefreshing = false
        error.visibility = View.VISIBLE
        empty.visibility = View.GONE
    }

    private class OrderAdapter(
            val onEmpty: () -> Unit,
            val onLoaded: () -> Unit,
            val onItemClick: (order: Order) -> Unit
    ) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

        public val data = mutableListOf<Order>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OrderViewHolder(parent)

        override fun onBindViewHolder(holder: OrderViewHolder, position: Int) = with (data[position]) {
            holder.title = title
            holder.summary = summary
            holder.price = price
            holder.status = Order.getStringValsByStatus(status)
            holder.itemView.setOnClickListener {
                onItemClick(this)
            }
        }

        override fun getItemCount(): Int {
            when (data.size) {
                0 -> onEmpty()
                else -> onLoaded()
            }
            return data.size
        }

        private class OrderViewHolder(parent: ViewGroup) :
                RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)) {
            private val titleView = itemView.findViewById<TextView>(android.R.id.title)
            private val summaryView = itemView.findViewById<TextView>(android.R.id.text1)
            private val statusView = itemView.findViewById<TextView>(android.R.id.text2)
            private val priceView = itemView.findViewById<TextView>(R.id.price)
            var title by TextViewStringDelegate(titleView)
            var summary by TextViewStringDelegate(summaryView)
            var price by TextViewPriceDelegate(priceView)
            var status = Order.StringVals.STATUS_WAITING_PAY
                set(@Order.Status value) {
                    field = value
                    statusView.text = itemView.resources.getText(value)
                }
        }
    }

}

