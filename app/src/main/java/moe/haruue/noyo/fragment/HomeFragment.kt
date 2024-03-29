package moe.haruue.noyo.fragment

import android.os.Bundle
import android.support.annotation.Keep
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.jude.rollviewpager.adapter.LoopPagerAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import moe.haruue.noyo.GoodsActivity
import moe.haruue.noyo.R
import moe.haruue.noyo.utils.startActivity


/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
@Keep
class HomeFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rollViewPager.setAdapter(object: LoopPagerAdapter(rollViewPager) {
            override fun getRealCount(): Int {
                return images.size
            }

            val images = arrayOf(R.drawable.pager_default_0, R.drawable.pager_default_1, R.drawable.pager_default_2, R.drawable.pager_default_3)

            override fun getView(container: ViewGroup, position: Int): View {
                val image = AppCompatImageView(container.context)
                image.setImageResource(images[position])
                image.scaleType = ImageView.ScaleType.CENTER_CROP
                image.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                return image
            }
        })
        buttonRentFarm.setOnClickListener {
            startActivity<GoodsActivity> {
                putExtra(GoodsActivity.EXTRA_TYPE, GoodsActivity.TYPE_RENT_FARM)
            }
        }
        buttonPreSell.setOnClickListener {
            startActivity<GoodsActivity> {
                putExtra(GoodsActivity.EXTRA_TYPE, GoodsActivity.TYPE_PRE_SELL)
            }
        }
    }

}