package osp.leobert.android.pagertrackerdemo.index.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import osp.leobert.android.pagertrackerdemo.BaseFragment
import osp.leobert.android.pagertrackerdemo.R
import osp.leobert.android.pagertrackerdemo.essay.EssayDetailActivity
import osp.leobert.android.tracker.pager.ITrackedPager
import osp.leobert.android.tracker.pager.TrackedPager

@TrackedPager(
    pagerPoint = "P_INDEX_FG",
    whenFragment = TrackedPager.FragmentStrategy.REPLACE_ACTIVITY
)
class HomeFragment : BaseFragment(), ITrackedPager.FragmentInViewPager {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_demo, container, false)
        val textView: TextView = root.findViewById(R.id.tv)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        textView.setOnClickListener {
            EssayDetailActivity.launch(it.context, "321")
        }

        return root
    }
}