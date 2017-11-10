package moe.haruue.noyo.fragment

import android.os.Bundle
import android.support.annotation.Keep
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moe.haruue.noyo.R

/**
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
@Keep
class AllFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_all, container, false)
    }

}