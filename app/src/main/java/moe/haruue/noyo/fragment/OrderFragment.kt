package moe.haruue.noyo.fragment

import android.os.Bundle
import android.support.annotation.IntDef
import android.support.annotation.Keep
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_order.*
import moe.haruue.noyo.App
import moe.haruue.noyo.R
import moe.haruue.noyo.api.ApiServices
import moe.haruue.noyo.model.Order
import moe.haruue.noyo.utils.TextViewPriceDelegate
import moe.haruue.noyo.utils.TextViewStringDelegate
import moe.haruue.noyo.utils.createApiSubscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
@Keep
class OrderFragment : BaseFragment() {

    private val adapter = OrderAdapter(this::onEmpty, this::onLoaded) {
        // TODO: on order item on click here
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
        ApiServices.v1service.listOrder()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createApiSubscriber {
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
                })
                .lifecycleUnsubscribe()
    }

    fun onEmpty() {
        list.visibility = View.GONE
        progress.isRefreshing = false
        error.visibility = View.GONE
        empty.visibility = View.VISIBLE
    }

    fun onLoaded() {
        list.visibility = View.VISIBLE
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
        list.visibility = View.GONE
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
            holder.status = when (status) {
                Order.STATUS_WAITING_PAY -> OrderFragment.STATUS_WAITING_PAY
                Order.STATUS_PAID -> OrderFragment.STATUS_PAID
                Order.STATUS_WAITING_PLANT -> OrderFragment.STATUS_WAITING_PLANT
                Order.STATUS_PLANTED -> OrderFragment.STATUS_PLANTED
                Order.STATUS_WAITING_HARVEST -> OrderFragment.STATUS_WAITING_HARVEST
                Order.STATUS_HARVESTED -> OrderFragment.STATUS_HARVESTED
                Order.STATUS_TRANSPORT -> OrderFragment.STATUS_TRANSPORT
                Order.STATUS_DELIVERED -> OrderFragment.STATUS_DELIVERED
                Order.STATUS_CANCELLED -> OrderFragment.STATUS_CANCELLED
                else -> throw IllegalArgumentException("If you add new status, please modify OrderAdapter.onBindViewHolder");
            }
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
            var status = STATUS_WAITING_PAY
                set(@Status value) {
                    field = value
                    statusView.setText(value)
                }
        }
    }

    companion object {
        const val STATUS_WAITING_PAY = R.string.status_waiting_pay
        const val STATUS_PAID = R.string.status_paid
        const val STATUS_WAITING_PLANT = R.string.status_waiting_plant
        const val STATUS_PLANTED = R.string.status_planted
        const val STATUS_WAITING_HARVEST = R.string.status_waiting_harvest
        const val STATUS_HARVESTED = R.string.status_harvested
        const val STATUS_TRANSPORT = R.string.status_transport
        const val STATUS_DELIVERED = R.string.status_delivered
        const val STATUS_CANCELLED = R.string.status_cancelled
    }

    @IntDef(STATUS_WAITING_PAY.toLong(),
            STATUS_PAID.toLong(),
            STATUS_WAITING_PLANT.toLong(),
            STATUS_PLANTED.toLong(),
            STATUS_WAITING_HARVEST.toLong(),
            STATUS_HARVESTED.toLong(),
            STATUS_TRANSPORT.toLong(),
            STATUS_DELIVERED.toLong(),
            STATUS_CANCELLED.toLong())
    @Retention(AnnotationRetention.SOURCE)
    annotation class Status
}

