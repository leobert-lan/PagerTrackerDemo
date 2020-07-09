package osp.leobert.android.pagertrackerdemo.index

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import osp.leobert.android.pagertrackerdemo.BaseActivity
import osp.leobert.android.pagertrackerdemo.R
import osp.leobert.android.pagertrackerdemo.index.ui.dashboard.DashboardFragment
import osp.leobert.android.pagertrackerdemo.index.ui.home.HomeFragment
import osp.leobert.android.pagertrackerdemo.index.ui.notifications.NotificationsFragment
import osp.leobert.android.pagertrackerdemo.launchActivity
import osp.leobert.android.tracker.pager.PagerChainTracker
import osp.leobert.android.tracker.pager.TrackedPager

//首页需要Fragment的P点，所以不再单独定义点号，拦截自动上传即可
@TrackedPager(pagerPoint = "",autoReport = false)
class IndexActivity : BaseActivity() {
    companion object {
        fun launch(context: Context) {
            Intent(context, IndexActivity::class.java).launchActivity(context)
        }
    }

    private var mViewPager2: ViewPager2? = null
    private var mTabLayout: TabLayout? = null
    private var mAdapter: ViewPagerFragmentStateAdapter? = null

    private val key_index = "key_index"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)

        mTabLayout = findViewById(R.id.tablayout)
        mViewPager2 = findViewById(R.id.viewpager2)
        mAdapter = ViewPagerFragmentStateAdapter(supportFragmentManager, lifecycle)

        mViewPager2?.let {
            it.offscreenPageLimit = 1
            it.adapter = mAdapter

            it.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mTabLayout?.setScrollPosition(position, 0f, false)
                }
            })
        }

        mTabLayout?.let {

            it.addTab(it.newTab().setText("Index"))
            it.addTab(it.newTab().setText("Notify"))
            it.addTab(it.newTab().setText("Dash"))

            it.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    mViewPager2?.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {
                    if (mViewPager2?.currentItem != tab.position) {
                        mViewPager2?.currentItem = tab.position
                    }
                }
            })
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val index = savedInstanceState.getInt(key_index)
        mTabLayout?.setScrollPosition(index, 0f, true)
        mViewPager2?.currentItem = index
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putInt(key_index, mTabLayout?.selectedTabPosition ?: 0)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun finish() {
        //对于一般的应用，主页面关闭基本就可以认为页面链全部清除了。
        PagerChainTracker.clearAll()
        super.finish()
    }


    internal class ViewPagerFragmentStateAdapter : FragmentStateAdapter {
        constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)
        constructor(
            fragmentManager: FragmentManager,
            lifecycle: Lifecycle
        ) : super(fragmentManager, lifecycle)

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> NotificationsFragment()
                else -> DashboardFragment()
            }
        }

        override fun getItemCount(): Int {
            return 3
        }
    }
}


