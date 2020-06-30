package osp.leobert.android.pagertrackerdemo

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import osp.leobert.android.pagertrackerdemo.index.IndexActivity
import osp.leobert.android.tracker.pager.TrackedPager

@TrackedPager(pagerPoint = "P_1")
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv: TextView = findViewById(R.id.tv)

        lifecycleScope.launch {
            async {
                delay(3000)
                IndexActivity.launch(this@MainActivity)
                finish()
            }

            async {
                delay(1500)
                "假装是splash页面"
            }.let {
                tv.text = it.await()
            }
        }
    }
}