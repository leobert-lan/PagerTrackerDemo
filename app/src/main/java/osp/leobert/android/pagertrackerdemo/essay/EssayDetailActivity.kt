package osp.leobert.android.pagertrackerdemo.essay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import kotlinx.android.synthetic.main.content_essay_detail.*
import osp.leobert.android.pagertrackerdemo.BaseActivity
import osp.leobert.android.pagertrackerdemo.R
import osp.leobert.android.pagertrackerdemo.bio.BioActivity
import osp.leobert.android.pagertrackerdemo.launchActivity
import osp.leobert.android.tracker.pager.PagerChainTracker
import osp.leobert.android.tracker.pager.TrackedPager

@TrackedPager(pagerPoint = "P_ESSAY_DETAIL", autoReport = false)
class EssayDetailActivity : BaseActivity() {

    companion object {
        const val EXT_STR_ID = "EXT_STR_ID"

        fun launch(context: Context, essayId: String) {
            Intent(context, EssayDetailActivity::class.java)
                .putExtra(EXT_STR_ID, essayId)
                .launchActivity(context)
        }
    }

    private val essayId: String by lazy {
        intent.getStringExtra(EXT_STR_ID) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_essay_detail)
        setSupportActionBar(findViewById(R.id.toolbar))


        PagerChainTracker.pressData(this, mutableListOf(Pair<String, String>("id", essayId)))
        PagerChainTracker.manualReport()

        val onAuthorClickedListener = View.OnClickListener {
            BioActivity.launch(this@EssayDetailActivity, "李白的id")
        }
        avatar.setOnClickListener(onAuthorClickedListener)
        tv_author.setOnClickListener(onAuthorClickedListener)
    }
}