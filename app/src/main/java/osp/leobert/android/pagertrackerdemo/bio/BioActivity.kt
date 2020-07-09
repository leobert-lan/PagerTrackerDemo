package osp.leobert.android.pagertrackerdemo.bio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import osp.leobert.android.pagertrackerdemo.BaseActivity
import osp.leobert.android.pagertrackerdemo.R
import osp.leobert.android.pagertrackerdemo.launchActivity
import osp.leobert.android.tracker.pager.TrackedPager

@TrackedPager(pagerPoint = "P_BIO")
class BioActivity : BaseActivity() {
    companion object {
        const val EXT_STR_ID = "EXT_STR_ID"
        fun launch(context: Context, userId: String) {
            Intent(context, BioActivity::class.java)
                .putExtra(EXT_STR_ID, userId)
                .launchActivity(context)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bio)
    }
}