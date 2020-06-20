package osp.leobert.android.tracker.pager

import android.util.Log
import android.util.Pair
import androidx.fragment.app.Fragment
import osp.leobert.android.tracker.BuryPoint
import osp.leobert.android.tracker.BuryPointContext
import java.util.concurrent.atomic.AtomicInteger

/**
 * <p><b>Package:</b> osp.leobert.android.tracker.pager </p>
 * <p><b>Classname:</b> PagerChainTracker </p>
 * Created by leobert on 2020/5/15.
 */
@Suppress("WeakerAccess", "unused")
class PagerChainTracker {
    internal interface DataBundle {
        fun pagerChain(): ArrayList<PagerEntity>
    }

    companion object {

        open class DummyPage() : ITrackedPager {
            var mPagerToken: String? = null

            override fun setPagerToken(pagerToken: String) {
                this.mPagerToken = pagerToken
            }

            override fun getPagerToken(): String = mPagerToken ?: ""

            fun onCreate() {
                val trackedPager = javaClass.getAnnotation(TrackedPager::class.java)
                if (trackedPager?.ignore == true)
                    return

                dataBundle?.pagerChain()?.let { pagerChain ->
                    pagerChain.add(createPagerEntity(this))
                    findBpContext(this)

                    //auto or manual report
                    trackedPager?.takeIf { tp -> tp.autoReport }?.let { _ ->
                        handleReport(this, pagerChain)
                    }
                }
            }

            fun onResume() {
                dataBundle?.pagerChain()?.let { pagerChain ->
                    pagerToken.let { token ->
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

            fun autoHandle() {
                if (pagerToken.isEmpty()) {
                    onCreate()
                } else {
                    onResume()
                }
            }

        }

        const val debug = true
        private val bpContexts: MutableMap<String, BuryPointContext> = hashMapOf()

        fun registerBp(token: String, bp: BuryPointContext) {
            bpContexts[token] = bp
        }

        fun unRegisterBp(token: String) {
            bpContexts.remove(token)
        }

        internal inline fun <reified R> Any?.takeIfInstance(): R? {
            if (this is R) return this
            return null
        }

        private const val handleMissingNotatedFragment = false

        const val tag = "pager_track"
        private val atomicInteger = AtomicInteger(0)

        internal var dataBundle: DataBundle? = null
            get() {
                if (field == null)
                    Log.d(tag, "dataBundle is null!")
                return field
            }

        //manual track
        fun track(pager: ITrackedPager) {
            dataBundle?.let {
                handleReport(pager, it.pagerChain())
            }
        }

        internal fun findBpContext(pager: ITrackedPager) {
            try {
                val methods = pager.javaClass.declaredMethods
                methods.forEach {
                    if (it.getAnnotation(TrackerBpContext::class.java) != null) {
                        it.isAccessible = true
                        registerBp(pager.pagerToken, it.invoke(pager) as BuryPointContext)
                        return@forEach
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "findBpContext error", e)
            }
        }

        fun helpFragmentStart(pager: ITrackedPager) {
            try {
                dataBundle?.let { bundle ->
                    val trackedPager = pager.javaClass.getAnnotation(TrackedPager::class.java)
                    when {
                        trackedPager != null -> {
                            pager.pagerToken.takeIf { it.isNotEmpty() }?.let { unRegisterBp(it) }
                            pager.pagerToken = createToken(pager)
                            findBpContext(pager)
                            trackedPager.whenFragment.manualAddChainNode(pager, trackedPager.autoReport)
                        }
                        handleMissingNotatedFragment -> {
                            bundle.pagerChain().add(createPagerEntity(pager))
                        }
                        else -> {
                            Log.v(tag, "not notated for fragment: ${debugPagerInfo(pager)}")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "error", e)
            }
        }

        //这里我们相信业务不会瞎来，不做严格的校验，简单校验后添加到链路中
        fun helpFragmentOnResumeInViewPager(pager: ITrackedPager) {
            try {
                pager.takeIfInstance<ITrackedPager.FragmentInViewPager>()?.let {
                    val trackedPager = pager.javaClass.getAnnotation(TrackedPager::class.java)
                    if (trackedPager?.whenFragment == TrackedPager.FragmentStrategy.REPLACE_ACTIVITY) {
                        TrackedPager.FragmentStrategy.REPLACE_ACTIVITY.manualAddChainNode(it, false/*trackedPager.autoReport*/)
                    } else {
                        Log.e(tag, "当前仅处理${TrackedPager.FragmentStrategy.REPLACE_ACTIVITY}的配置")
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "error", e)
            }
        }

        fun helpFragmentDestroy(pager: ITrackedPager) {
            try {
                pager.pagerToken.let { token ->
                    dataBundle?.pagerChain()?.let { pagerChain ->
                        pager.pagerToken.takeIf { it.isNotEmpty() }?.let { unRegisterBp(it) }

                        val trackedPager = pager.javaClass.getAnnotation(TrackedPager::class.java)
                        if (trackedPager != null || handleMissingNotatedFragment) {
                            if (pagerChain.size > 1 && pagerChain[pagerChain.size - 1].pagerToken == token) {
                                pagerChain.removeAt(pagerChain.size - 1)
                            } else {
                                Log.i(tag, "want to remove last:${debugPagerInfo(pager)},but size is ${pagerChain.size} and last is ${pagerChain.lastOrNull()}")
                            }
                        } else {
                            Log.v(tag, "not notated for fragment: ${debugPagerInfo(pager)}")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "error", e)
            }
        }

        fun replacePagerInfo(pager: ITrackedPager, from: ITrackedPager, report: Boolean = false) {
            try {
                dataBundle?.let { bundle ->
                    bundle.pagerChain().find { it.pagerToken == pager.pagerToken }?.let {
                        val target = createPagerEntity(from, from.pagerToken.isEmpty())
                        it.pagerPoint = target.pagerPoint
                        it.reserveConfig.clear()
                        it.reserveConfig.putAll(target.reserveConfig)
                        it.data.clear()
                        it.bpToken = from.pagerToken

                        if (report)
                            handleReport(pager, pagerChain = bundle.pagerChain())
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "error", e)
            }
        }

        //慎用！
        fun replacePagerInfo(pager: ITrackedPager, from: PagerEntity, report: Boolean = false) {
            try {
                dataBundle?.let { bundle ->
                    bundle.pagerChain().find { it.pagerToken == pager.pagerToken }?.let {
                        it.pagerPoint = from.pagerPoint
                        it.reserveConfig.clear()
                        it.reserveConfig.putAll(from.reserveConfig)
                        it.data.clear()
                        it.bpToken = from.pagerToken

                        if (report)
                            handleReport(pager, pagerChain = bundle.pagerChain())
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "error", e)
            }
        }

        /**
         * 手动添加一个节点，e.g.:弹窗显示
         * [report] true if report the point right now
         * */
        fun manualAddChainNode(pagerEntity: PagerEntity, report: Boolean = false) {
            try {
                dataBundle?.let {
                    it.pagerChain().add(pagerEntity)
                    if (report)
                        reportPagers(it.pagerChain())
                }
            } catch (e: Exception) {
                Log.e(tag, "error", e)
            }
        }

        fun manualAddChainNode(pager: ITrackedPager, report: Boolean = false) {
            try {
                dataBundle?.let {
                    val trackedPager = pager.javaClass.getAnnotation(TrackedPager::class.java)
                    if (trackedPager != null && trackedPager.autoReport && report) {
                        val stackInfo = Throwable()
                        Log.i(tag, "[bug?] " + debugPagerInfo(pager) + " is auto report, but manual report is called!", stackInfo)
                    }

                    if (trackedPager != null && pager is Fragment) {
                        trackedPager.whenFragment.manualAddChainNode(pager, report)
                    } else {
                        manualAddChainNode(createPagerEntity(pager), report)
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "error", e)
            }
        }

        private fun createToken(obj: Any): String {
            return obj.javaClass.simpleName + "_" + atomicInteger.getAndIncrement()
        }

        fun createPagerEntity(pager: ITrackedPager): PagerEntity {
            return createPagerEntity(pager, true)
        }

        fun createPagerEntity(pager: ITrackedPager, newToken: Boolean = true): PagerEntity {
            val token = if (newToken) createToken(pager) else pager.pagerToken

            val pagerEntity = PagerEntity(pagerToken = token, data = arrayListOf())

            val trackedPager = pager.javaClass.getAnnotation(TrackedPager::class.java)
            if (trackedPager != null) {
                pagerEntity.pagerPoint = trackedPager.pagerPoint.takeIf { pn -> pn.isNotEmpty() }
                        ?: "未设置(${pager.javaClass.simpleName})"

                trackedPager.reserveConfig.forEach { config ->
                    pagerEntity.reserveConfig[config.on] = config.asPoint
                }

                pagerEntity.reserveLimit = trackedPager.reserveLimit
            } else {
                pagerEntity.pagerPoint = "未设置(${pager.javaClass.simpleName})"
            }
            pager.pagerToken = token
            return pagerEntity
        }

        /**
         * 以A->B->C为例，当前在C，注意调用时机至少是onCreate，准确的说，是页面被采集入栈，注意手动入栈的情况（例如文章详情页，可能P点是不一致的，就需要手动入栈）
         * limit 一般取1就可以了，向前面倒推的页面数量。如果是2，会返回 "A - B"，如果<1,会返回全部页面链，
         * 返回的Pair，first是全部页面链，second是按照数量倒推的页面，e.g. limit=1 return:["A - B -C","B"],limit =2: ["A - B - C","A - B"]
         * */
        fun currentPagerChain(limit: Int = 1): Pair<String, String> {
            try {
                dataBundle?.let {
                    val chain = it.pagerChain()
                    if (chain.size > 0) {
                        return PagerEntity.formatPagerChain(chain, if (limit < 1) Int.MAX_VALUE else limit)
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "error", e)
            }
            return Pair("", "")
        }

        fun pressData(pager: ITrackedPager, data: MutableList<Pair<String, String>>) {
            try {
                dataBundle?.let {
                    it.pagerChain().forEach { e ->
                        if (e.pagerToken == pager.pagerToken) {
                            //先全部添加，如果有必要，我们做差分
                            e.data.addAll(data)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "error", e)
            }
        }

        fun clearAll() {
            try {
                dataBundle?.pagerChain()?.clear()
            } catch (e: Exception) {
                Log.e(tag, "error", e)
            }
        }

        internal fun handleReport(pager: ITrackedPager, pagerChain: ArrayList<PagerEntity>) {
            //初期我们搞点log验证下，方便查bug，设计中，是可以直接上报了
            //毕竟没啥正常场景会出现：下一个页面都加载了，上一个页面才报页面点
            pager.pagerToken.let { token ->
                if (pagerChain.size < 1 || pagerChain[pagerChain.size - 1].pagerToken != token) {
                    Log.d(tag, "[bug?] ${debugPagerInfo(pager)} want report pager point," +
                            " but size is ${pagerChain.size} and last is ${pagerChain.lastOrNull()} ")
                }

                if (pagerChain.size > 0 /*&& pagerChain[pagerChain.size - 1].pagerToken == token*/) {
                    reportPagers(pagerChain)
                }
            }
        }

        /**
         * 假定当前的页面已经入栈了，这一点需要业务自己控制了！
         * */
        fun manualReport() {
            dataBundle?.let {
                reportPagers(it.pagerChain())
            }
        }

        private fun reportPagers(pagerChain: ArrayList<PagerEntity>) {
            try {
                if (pagerChain.isEmpty()) {
                    Log.e(tag, "try report pager chain but is empty")
                }

                val limit = pagerChain[pagerChain.size - 1].reserveLimit

                val chain = PagerEntity.formatPagerChain(pagerChain, if (limit < 1) Int.MAX_VALUE else limit)
                val lastNode = pagerChain[pagerChain.size - 1]

                val bpToken = lastNode.bpToken.takeIf { it.isNotEmpty() } ?: lastNode.pagerToken

                val bpContext = bpContexts[bpToken]
                if (bpContext != null) {
                    bpContext.track(BuryPoint.normal(lastNode.pagerPoint), true, pagerChain[pagerChain.size - 1].data.apply {
                        //this.add(Pair("chain", chain.first))
                        this.add(Pair("frompage", chain.second))
                    })
                } else {
                    uploadPoint(lastNode.pagerPoint, pagerChain[pagerChain.size - 1].data.apply {
                        //this.add(Pair("chain", chain.first))
                        this.add(Pair("frompage", chain.second))
                    })
                }


                Log.d(tag, "report pager:${pagerChain[pagerChain.size - 1].pagerPoint},from: ${chain.second}, chain: ${chain.first}")
            } catch (e: Exception) {
                Log.e(tag, "report pager", e)
            }
        }

        internal fun debugPagerInfo(pager: ITrackedPager): String {
            val pagerEntity = PagerEntity(pagerToken = "token", data = arrayListOf())

            val trackedPager = pager.javaClass.getAnnotation(TrackedPager::class.java)
            if (trackedPager != null) {
                pagerEntity.pagerPoint = trackedPager.pagerPoint.takeIf { pn -> pn.isNotEmpty() } ?: "未设置(${pager.javaClass.simpleName})"

                trackedPager.reserveConfig.forEach { config ->
                    pagerEntity.reserveConfig[config.on] = config.asPoint
                }
            }
            return "${pager.javaClass.simpleName} $pagerEntity"
        }

        internal fun printChain() {
            Log.v(tag, " ")
            Log.v(tag, " ")
            Log.v(tag, "print chain start========")
            dataBundle?.pagerChain()?.forEachIndexed { i, e ->
                Log.v(tag, "$i:${e}")
            }

            Log.v(tag, "print chain end=====")
            Log.v(tag, " ")
            Log.v(tag, " ")
        }

        private fun uploadPoint(pointKey: String?, params: List<Pair<String, String>>?) {
            BuryPointContext.buryPointUploader?.upload(pointKey, params)
        }
    }
}