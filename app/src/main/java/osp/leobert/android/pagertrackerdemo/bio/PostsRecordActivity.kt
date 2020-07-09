package osp.leobert.android.pagertrackerdemo.bio

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import osp.leobert.android.pagertrackerdemo.BaseActivity
import osp.leobert.android.pagertrackerdemo.R
import osp.leobert.android.tracker.pager.TrackedPager

/**
 * 演示Fragment不需要页面点的情况。
 * */
@TrackedPager(pagerPoint = "P_POSTS_RECORD")
class PostsRecordActivity : BaseActivity() {

    private var mViewPager2: ViewPager2? = null
    private var mTabLayout: TabLayout? = null
    private var mAdapter: ViewPagerFragmentStateAdapter? = null

    private val key_index = "key_index"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts_record)
        mTabLayout = findViewById(R.id.tablayout)
        mViewPager2 = findViewById(R.id.viewpager2)
        mAdapter = ViewPagerFragmentStateAdapter(supportFragmentManager, lifecycle)

        mViewPager2?.let {
            it.offscreenPageLimit = 1
            it.adapter = mAdapter

            it.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mTabLayout?.setScrollPosition(position, 0f, false)
                }
            })
        }

        mTabLayout?.let {

            it.addTab(it.newTab().setText("我发布的文章"))
            it.addTab(it.newTab().setText("我发布的动态"))

            it.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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

}

private class DemoFragment : Fragment() {
    companion object {
        const val BUNDLE_STR_PAGE_NAME = "BUNDLE_STR_PAGE_NAME"

        fun newInstance(name: String): Fragment {
            return DemoFragment().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_STR_PAGE_NAME, name)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rl = RelativeLayout(inflater.context)
        rl.addView(TextView(inflater.context).apply {
            id = R.id.id_tv
        }, RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        return rl
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tv = view.findViewById<TextView>(R.id.id_tv)
        tv.text = arguments?.getString(BUNDLE_STR_PAGE_NAME) ?: "missing name"

    }
}

class ViewPagerFragmentStateAdapter : FragmentStateAdapter {
    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)
    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(fragmentManager, lifecycle)

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DemoFragment.newInstance("我发布的文章")
            else -> DemoFragment.newInstance("我发布的动态")
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}