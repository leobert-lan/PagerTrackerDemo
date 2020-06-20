package osp.leobert.android.tracker.pager

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import osp.leobert.android.tracker.pager.PagerChainTracker.Companion.takeIfInstance

/**
 * <p><b>Package:</b> osp.leobert.android.tracker.pager </p>
 * <p><b>Classname:</b> PagerTrackerLifecycleCallbacks </p>
 * Created by leobert on 2020/5/14.
 */
class PagerTrackerLifecycleCallbacks : ActivityLifecycleCallbacks {
    val tag = PagerChainTracker.tag

    private class DebugImpl:ArrayList<PagerEntity>() {
        override fun add(element: PagerEntity): Boolean {
            return super.add(element).apply {
                PagerChainTracker.printChain()
            }
        }
    }

    private val pagerChain: ArrayList<PagerEntity> = if (PagerChainTracker.debug) DebugImpl() else arrayListOf()

    private val dataBundle: PagerChainTracker.DataBundle = object : PagerChainTracker.DataBundle {
        override fun pagerChain(): ArrayList<PagerEntity> {
            return pagerChain
        }
    }

    init {
        PagerChainTracker.dataBundle = dataBundle
    }


    private val bundle_key_str_pager_token = "bundle_key_str_pager_token"

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activity.takeIfInstance<ITrackedPager>()?.let {
            if (savedInstanceState == null) {
                val trackedPager = activity.javaClass.getAnnotation(TrackedPager::class.java)
                if (trackedPager?.ignore == true)
                    return

                pagerChain.add(PagerChainTracker.createPagerEntity(it))
                PagerChainTracker.findBpContext(it)

                //auto or manual report
                trackedPager?.takeIf { tp -> tp.autoReport }?.let { _ ->
                    PagerChainTracker.handleReport(it, pagerChain)
                }

            } else {
                savedInstanceState.getString(bundle_key_str_pager_token)?.let { token ->
                    it.pagerToken = token
                }
                PagerChainTracker.findBpContext(it)
            }

            //先不搞这个，有风险
//            if (activity is FragmentActivity) {
//                val tmp = activity.supportFragmentManager
//                //可能会影响到一些系统的默认处理
//                tmp.fragmentFactory = FragmentFactoryWrapper(tmp.fragmentFactory)
//            } else {
//                Log.i(tag,"如有必要，它使用的Fragment需要单独配置生命周期检测，$activity")
//            }
        }
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        activity.takeIfInstance<ITrackedPager>()?.let {
            it.pagerToken.let { token ->
                var i = pagerChain.size - 1
                while (i >= 0 && pagerChain[i].pagerToken != token) {
                    i--
                }

                while (i >= 0 && i < pagerChain.size - 1) {
                    pagerChain.removeAt(pagerChain.size - 1)
                }
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        activity.takeIfInstance<ITrackedPager>()?.let {
            outState.putString(bundle_key_str_pager_token, it.pagerToken)
        }
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        activity.takeIfInstance<ITrackedPager>()?.let {
            it.pagerToken.let { token ->
                if (pagerChain.size > 1 && pagerChain[pagerChain.size - 1].pagerToken == token) {
                    pagerChain.removeAt(pagerChain.size - 1)
                } else {
                    Log.i(tag, "want to remove last:${PagerChainTracker.debugPagerInfo(it)},but size is ${pagerChain.size} and last is ${pagerChain.lastOrNull()}")
                }
                PagerChainTracker.unRegisterBp(token)
            }
        }
    }
}