package moe.haruue.noyo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import moe.haruue.noyo.api.ApiServices
import moe.haruue.noyo.model.Member
import moe.haruue.noyo.utils.createApiSubscriber
import moe.haruue.noyo.utils.toast
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class ProvinceChooserActivity : BaseActivity() {

    companion object {
        const val EXTRA_PROVINCE = "province"
    }

    val recycler by lazy {
        val r = RecyclerView(this);
        r.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT)
        r
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(recycler)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = ProvinceItemAdapter {
            ApiServices.v1service.accountUpdate("city", it)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createApiSubscriber {
                        onNext = {
                            App.instance.member = it.data ?: App.instance.member
                            toast("更改地区信息成功")
                        }
                        onApiError = {
                            toast("暂时无法更改您所在的城市，请稍后再试")
                        }
                    })
            App.instance.member.province = it
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_PROVINCE, it)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }


    class ProvinceItemAdapter(
            private val onProvinceSelectedListener: (province: String) -> Unit
    ) : RecyclerView.Adapter<ProvinceItemAdapter.ProvinceItemViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProvinceItemViewHolder(parent)

        override fun getItemCount() = Member.PROVINCES.size

        override fun onBindViewHolder(holder: ProvinceItemViewHolder, position: Int) {
            holder.text.text = Member.PROVINCES[position]
            holder.setOnClickListener {
                onProvinceSelectedListener(Member.PROVINCES[position])
            }

        }

        class ProvinceItemViewHolder(parent: ViewGroup) :
                RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false)) {
            val text = itemView.findViewById<TextView>(android.R.id.text1)
            fun setOnClickListener(listener: (v: View) -> Unit) = itemView.setOnClickListener(listener)
        }
    }
}