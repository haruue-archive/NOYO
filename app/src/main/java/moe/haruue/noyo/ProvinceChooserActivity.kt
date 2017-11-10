package moe.haruue.noyo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import moe.haruue.noyo.model.User
import moe.haruue.noyo.utils.logd

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class ProvinceChooserActivity : AppCompatActivity() {

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
            logd("ProvinceItemAdapter: province=$it")
            App.instance.user.province = it
            App.instance.user.sync(true)
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

        override fun getItemCount() = User.PROVINCES.size

        override fun onBindViewHolder(holder: ProvinceItemViewHolder, position: Int) {
            holder.text.text = User.PROVINCES[position]
            holder.setOnClickListener {
                onProvinceSelectedListener(User.PROVINCES[position])
            }

        }

        class ProvinceItemViewHolder(parent: ViewGroup) :
                RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false)) {
            val text = itemView.findViewById<TextView>(android.R.id.text1)
            fun setOnClickListener(listener: (v: View) -> Unit) = itemView.setOnClickListener(listener)
        }
    }
}