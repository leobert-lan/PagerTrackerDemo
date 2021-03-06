package osp.leobert.android.pagertrackerdemo.index.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import osp.leobert.android.pagertrackerdemo.BaseFragment
import osp.leobert.android.pagertrackerdemo.R
import osp.leobert.android.tracker.pager.ITrackedPager
import osp.leobert.android.tracker.pager.TrackedPager


@TrackedPager(
    pagerPoint = "P_DASH_FG",
    whenFragment = TrackedPager.FragmentStrategy.REPLACE_ACTIVITY
)
class DashboardFragment : BaseFragment(), ITrackedPager.FragmentInViewPager {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_demo, container, false)
        val textView: TextView = root.findViewById(R.id.tv)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}