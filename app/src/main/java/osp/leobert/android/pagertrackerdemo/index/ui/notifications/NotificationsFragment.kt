package osp.leobert.android.pagertrackerdemo.index.ui.notifications

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
    pagerPoint = "P_NOTIFY_FG",
    whenFragment = TrackedPager.FragmentStrategy.REPLACE_ACTIVITY
)
class NotificationsFragment : BaseFragment(), ITrackedPager.FragmentInViewPager {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_demo, container, false)
        val textView: TextView = root.findViewById(R.id.tv)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}