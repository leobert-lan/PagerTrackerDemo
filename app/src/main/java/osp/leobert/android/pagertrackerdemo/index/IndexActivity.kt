package osp.leobert.android.pagertrackerdemo.index

import android.content.Context
import android.content.Intent
import android.os.Bundle
import osp.leobert.android.pagertrackerdemo.BaseActivity
import osp.leobert.android.pagertrackerdemo.R
import osp.leobert.android.pagertrackerdemo.launchActivity
import osp.leobert.android.tracker.pager.TrackedPager
import com.google.android.material.tabs.TabLayout

//@TrackedPager 首页需要Fragment的P点，所以不再单独定义
class IndexActivity : BaseActivity() {
    companion object {
        fun launch(context:Context) {
            Intent(context,IndexActivity::class.java).launchActivity(context)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)
    }
}


