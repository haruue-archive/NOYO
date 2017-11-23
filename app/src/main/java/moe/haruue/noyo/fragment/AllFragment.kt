package moe.haruue.noyo.fragment

import android.os.Bundle
import android.support.annotation.Keep
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_all.*
import moe.haruue.noyo.R
import moe.haruue.noyo.model.Goods
import moe.haruue.noyo.utils.TextViewPriceDelegate
import moe.haruue.noyo.utils.TextViewStringDelegate

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
@Keep
class AllFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_all, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.layoutManager = LinearLayoutManager(activity)
        list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        list.adapter = ThingsAdapter()
    }

    private class ThingsAdapter : RecyclerView.Adapter<ThingsAdapter.BaseViewHolder>() {

        companion object {
            const val VT_HEADER = 0x0
            const val VT_PRICE = 0x1
            val PRICE_DATA = arrayOf(
                    Goods("白鲢活鱼", 6.5),
                    Goods("白萝卜", 0.55),
                    Goods("白条鸡", 18.0),
                    Goods("菜油（散装）", 10.8),
                    Goods("葱头", 2.0),
                    Goods("大白菜", 1.1),
                    Goods("大葱", 3.2),
                    Goods("大米", 3.72),
                    Goods("冬瓜", 1.2),
                    Goods("豆角", 6.2),
                    Goods("富士苹果", 9.8),
                    Goods("胡萝卜", 2.4),
                    Goods("黄瓜", 3.7),
                    Goods("活草鱼", 15.5)
            )
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
            VT_HEADER -> HeaderViewHolder(parent, viewType)
            VT_PRICE -> PriceItemViewHolder(parent, viewType)
            else -> throw IllegalArgumentException("If you use new view type, please edit AllFragment\$ThingsAdapter\$onCreateViewHolder.")
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            when (holder) {
                is HeaderViewHolder -> {}
                is PriceItemViewHolder -> {
                    val index = position - 1
                    holder.name = PRICE_DATA[index].title
                    holder.price = PRICE_DATA[index].price
                }
            }
        }

        override fun getItemCount() = PRICE_DATA.size + 1

        override fun getItemViewType(position: Int) = when (position) {
            0 -> VT_HEADER
            else -> VT_PRICE
        }

        private class PriceItemViewHolder(parent: ViewGroup, viewType: Int) : BaseViewHolder(parent, R.layout.item_all_price, viewType) {
            private val nameView = itemView.findViewById<TextView>(android.R.id.text1)
            private val priceView = itemView.findViewById<TextView>(android.R.id.text2)
            var name by TextViewStringDelegate(nameView)
            var price by TextViewPriceDelegate(priceView)
        }

        private class HeaderViewHolder(parent: ViewGroup, viewType: Int) : BaseViewHolder(parent, R.layout.item_all_header, viewType)

        private open class BaseViewHolder(parent: View, val viewType: Int) : RecyclerView.ViewHolder(parent) {
            constructor(parent: ViewGroup, @LayoutRes layout: Int, viewType: Int) :
                    this(LayoutInflater.from(parent.context). inflate(layout, parent, false), viewType)
        }

    }

}