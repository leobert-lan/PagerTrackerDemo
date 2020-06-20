package osp.leobert.android.tracker.pager

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import osp.leobert.android.tracker.pager.PagerChainTracker.Companion.takeIfInstance

/**
 * <p><b>Package:</b> osp.leobert.android.tracker.pager </p>
 * <p><b>Classname:</b> FragmentTrackerImpl </p>
 * Created by leobert on 2020/5/19.
 *
 * 理论上各种复杂情况都应该走手动模式，onStart中处理上报的，理论上都是当做单独的点，排除掉ViewPager中各种切换的情况。
 *
 * 考虑到便捷，会尝试下再定义一个标识接口，用于Fragment在多个ViewPager中到处切换
 */
class FragmentTrackerImpl : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

        if (event == Lifecycle.Event.ON_START) {
            source.takeIfInstance<ITrackedPager>()?.let {
                PagerChainTracker.helpFragmentStart(it)
            }
        }


        if (event == Lifecycle.Event.ON_DESTROY) {
            source.takeIfInstance<ITrackedPager>()?.let {
                PagerChainTracker.helpFragmentDestroy(it)
            }
        }
    }
}