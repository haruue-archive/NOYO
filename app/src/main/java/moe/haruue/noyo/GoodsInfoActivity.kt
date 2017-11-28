package moe.haruue.noyo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_goods_info.*
import moe.haruue.noyo.api.ApiServices
import moe.haruue.noyo.model.APIErrorList
import moe.haruue.noyo.model.Goods
import moe.haruue.noyo.model.Member
import moe.haruue.noyo.utils.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class GoodsInfoActivity : BaseActivity() {

    companion object {
        const val EXTRA_GOODS = "goods"
    }

    var goods by ParcelableExtraDelegate(this, EXTRA_GOODS, Goods())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_info)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (goods.id.isEmpty()) {
            finish()
            return
        }

        when(App.instance.member.role) {
            Member.ROLE_CONSUMER -> {
                consumerPanel.visibility = View.VISIBLE
                ownerPanel.visibility = View.GONE
                noPermissionTip.visibility = View.GONE
            }
            Member.ROLE_FARMER -> {
                if (goods.seller == App.instance.member.id) {
                    consumerPanel.visibility = View.GONE
                    ownerPanel.visibility = View.VISIBLE
                    noPermissionTip.visibility = View.GONE
                } else {
                    consumerPanel.visibility = View.GONE
                    ownerPanel.visibility = View.GONE
                    noPermissionTip.visibility = View.VISIBLE
                }
            }
        }

        contact.setOnClickListener {
        }

        buy.setOnClickListener {
            startActivity<CreateOrderActivity> {
                putExtra(CreateOrderActivity.EXTRA_GOODS, goods)
                putExtra(CreateOrderActivity.EXTRA_ACTION, CreateOrderActivity.ACTION_CREATE)
            }
        }

        delete.setOnClickListener {
            confirm("删除商品", "此操作不可撤销，确认删除此商品吗？") { _, _ ->
                ApiServices.v1service.removeGoods(goods.id)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(createApiSubscriber {
                            onNext = {
                                toast("删除商品成功")
                                finish()
                            }
                        })
                        .lifecycleUnsubscribe()
            }.show()
        }

        edit.setOnClickListener {
            startActivity<CreateGoodsActivity> {
                putExtra(CreateGoodsActivity.EXTRA_GOODS, goods)
                putExtra(CreateGoodsActivity.EXTRA_ACTION, CreateGoodsActivity.ACTION_EDIT)
            }
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
        supportActionBar?.title = "商品详情 - ${goods.title}"

        if (!goods.image.isNullOrEmpty()) {
            Glide.with(this)
                    .load(goods.image).into(imageView)
                    .onLoadFailed(resources.getDrawable(R.drawable.ph_no_pic))
        }

        titleView.text = goods.title
        summaryView.text = goods.summary
        addressView.text = goods.address
        countView.text = "剩余 ${goods.count} 件"
    }

    fun refresh() {
        ApiServices.v1service.infoGoods(goods.id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createApiSubscriber {
                    onStart = {
                        progress.isRefreshing = true
                    }
                    onNext = {
                        goods = it.data ?: goods
                    }
                    onApiError = {
                        when (it.errno) {
                            APIErrorList.noSuchGoods -> {
                                toast("此商品已不存在", Toast.LENGTH_LONG)
                                finish()
                            }
                        }
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