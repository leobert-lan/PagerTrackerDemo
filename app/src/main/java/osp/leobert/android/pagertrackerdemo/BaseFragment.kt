package osp.leobert.android.pagertrackerdemo

import androidx.fragment.app.Fragment
import osp.leobert.android.tracker.pager.ITrackedPager
import osp.leobert.android.tracker.pager.PagerChainTracker

/**
 * <p><b>Package:</b> osp.leobert.android.pagertrackerdemo </p>
 * <p><b>Project:</b> PagerTrackerDemo </p>
 * <p><b>Classname:</b> BaseFragment </p>
 * Created by leobert on 2020/6/30.
 */
abstract class BaseFragment : Fragment(), ITrackedPager {
    private var mPagerToken: String? = null

    override fun setPagerToken(pagerToken: String) {
        mPagerToken = pagerToken
    }

    override fun getPagerToken(): String = mPagerToken ?: ""

    private var firstOnResume = true

    override fun onResume() {
        super.onResume()
        if (firstOnResume) {
            firstOnResume = false
            PagerChainTracker.helpFragmentStart(this)
        } else {
            PagerChainTracker.helpFragmentOnResumeInViewPager(this)
        }
    }

    override fun onDestroy() {
        PagerChainTracker.helpFragmentDestroy(this)
        super.onDestroy()
    }
}