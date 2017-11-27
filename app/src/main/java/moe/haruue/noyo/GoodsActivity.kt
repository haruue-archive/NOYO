package moe.haruue.noyo

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_goods.*
import moe.haruue.noyo.api.ApiServices
import moe.haruue.noyo.model.Goods
import moe.haruue.noyo.model.Member
import moe.haruue.noyo.utils.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class GoodsActivity : BaseActivity() {

    private val adapter = GoodsAdapter(this::onEmpty, this::onLoaded) {
        startActivity<GoodsInfoActivity> {
            putExtra(GoodsInfoActivity.EXTRA_GOODS, it)
        }
    }

    companion object {
        const val EXTRA_TYPE = "type"
        const val TYPE_RENT_FARM = "mud"
        const val TYPE_PRE_SELL = "product"
    }

    val type by StringExtraDelegate(intent, EXTRA_TYPE, TYPE_PRE_SELL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods)
        when(type) {
            TYPE_PRE_SELL -> {
                supportActionBar?.title = "产品预售"
            }
            TYPE_RENT_FARM -> {
                supportActionBar?.title = "土地租赁"
            }
        }
        when(App.instance.member.role) {
            Member.ROLE_FARMER -> fab.visibility = View.VISIBLE
            Member.ROLE_CONSUMER -> fab.visibility = View.GONE
        }
        progress.setOnRefreshListener { refresh() }
        list.layoutManager = LinearLayoutManager(this@GoodsActivity)
        list.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        onLoading()
        refresh()
    }

    fun refresh() {
        ApiServices.v1service.listGoods(type)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createApiSubscriber {
                    onNext = {
                        adapter.data.clear()
                        val data = it.data ?: mutableListOf()
                        if (data.isEmpty()) {
                            onEmpty()
                        }
                        adapter.data.addAll(data)
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

    private class GoodsAdapter(
            private val onEmpty: () -> Unit,
            private val onLoaded: () -> Unit,
            private val onItemClick: (goods: Goods) -> Unit
    ) : RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder>() {

        val data = mutableListOf<Goods>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GoodsViewHolder(parent)

        override fun onBindViewHolder(holder: GoodsViewHolder, position: Int) {
            with(data[position]) {
                holder.title = title
                holder.summary = summary
                holder.price = price
                holder.itemView.setOnClickListener { onItemClick(this) }
            }
        }

        override fun getItemCount(): Int {
            when(data.size) {
                0 -> onEmpty()
                else -> onLoaded()
            }
            return data.size
        }

        private class GoodsViewHolder(parent: ViewGroup) :
                RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_goods, parent, false)) {

            private val titleView = itemView.findViewById<TextView>(android.R.id.title)
            private val summaryView = itemView.findViewById<TextView>(android.R.id.text1)
            private val priceView = itemView.findViewById<TextView>(R.id.price)
            var title by TextViewStringDelegate(titleView)
            var summary by TextViewStringDelegate(summaryView)
            var price by TextViewPriceDelegate(priceView)

        }
    }

}