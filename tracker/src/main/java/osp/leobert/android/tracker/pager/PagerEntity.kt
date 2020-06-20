package osp.leobert.android.tracker.pager

import android.util.Pair

/**
 * <p><b>Package:</b> osp.leobert.android.tracker.pager </p>
 * <p><b>Classname:</b> PagerEntity </p>
 * Created by leobert on 2020/5/14.
 */
class PagerEntity(val pagerToken: String, var pagerPoint: String = "未设置",
                  internal var reserveLimit: Int = 1,
                  val reserveConfig: MutableMap<String, String> = hashMapOf(),
                  var data: MutableList<Pair<String, String>>,
                  var bpToken: String = "") {

    companion object {
//        fun formatPagerChain(pagerChain: ArrayList<PagerEntity>): String {
//            var i = pagerChain.size - 1
//
//            var chain = " - "
//
//            var p: String? = null
//            while (i >= 0) {
//                chain = if (p == null) {
//                    " - " + pagerChain[i].pagerPoint + chain
//                } else {
//                    " - " + (pagerChain[i].reserveConfig[p] ?: pagerChain[i].pagerPoint) + chain
//                }
//                p = pagerChain[i].pagerPoint
//                i--
//            }
//            return chain.substring(0, chain.length - 3)
//        }

        fun formatPagerChain(pagerChain: ArrayList<PagerEntity>, limit: Int): Pair<String, String> {
            var i = pagerChain.size - 1

            var chain = ""
            var subChain = ""

            var p: String? = null
            while (i >= 0) {
                val cur = if (p == null) {
                    pagerChain[i].pagerPoint
                } else {
                    pagerChain[i].reserveConfig[p] ?: pagerChain[i].pagerPoint
                }

                chain = " - $cur$chain"


                if (pagerChain.size - i - 1 in 1..limit) {
                    subChain = " - $cur$subChain"
                }

                p = pagerChain[i].pagerPoint
                i--
            }
            val all = if (chain.length > 3) chain.substring(3) else chain
            subChain = if (subChain.length > 3) subChain.substring(3) else subChain
            return Pair(all, subChain)
        }
    }

    override fun toString(): String {
        return "PagerEntity(pagerToken='$pagerToken', pagerPoint='$pagerPoint', reserveLimit=$reserveLimit, reserveConfig=$reserveConfig, data=$data, bpToken='$bpToken')"
    }
}