package osp.leobert.android.pagertrackerdemo

import androidx.appcompat.app.AppCompatActivity
import osp.leobert.android.tracker.pager.ITrackedPager

/**
 * <p><b>Package:</b> osp.leobert.android.pagertrackerdemo </p>
 * <p><b>Project:</b> PagerTrackerDemo </p>
 * <p><b>Classname:</b> BaseActivity </p>
 * Created by leobert on 2020/6/30.
 */
abstract class BaseActivity : AppCompatActivity(), ITrackedPager {
    private var mPagerToken: String? = null

    override fun setPagerToken(pagerToken: String) {
        mPagerToken = pagerToken
    }

    override fun getPagerToken(): String = mPagerToken ?: ""
}