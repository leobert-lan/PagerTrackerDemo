package osp.leobert.android.pagertrackerdemo.essay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import osp.leobert.android.pagertrackerdemo.BaseActivity
import osp.leobert.android.pagertrackerdemo.R
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

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        PagerChainTracker.pressData(this, mutableListOf(Pair<String, String>("id", essayId)))
        PagerChainTracker.manualReport()
    }
}