package osp.leobert.android.tracker.pager;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p><b>Package:</b> osp.leobert.android.tracker.pager </p>
 * <p><b>Classname:</b> TrackedPager </p>
 * Created by leobert on 2020/5/14.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackedPager {
    String pagerPoint();

    ReserveConfig[] reserveConfig() default {};

    boolean autoReport() default true;

    FragmentStrategy whenFragment() default FragmentStrategy.ALONE;

    /**
     * @return the max limit of pagers backing-retrieve tracking, tracking all if less than 1
     */
    int reserveLimit() default 1;

//    /**
//     * @return true if ignore the page in the chain,even it has implemented {@link ITrackedPager}
//    目前通过忽略未配置的fragment处理，如果有必要再扩展这个配置,
    //目前用于activity，如果是true，不会加到chain中，也不会处理上报，
//     */
    boolean ignore() default false;

    enum FragmentStrategy {
        /**
         * 替代宿主Activity的点
         */
        REPLACE_ACTIVITY {
            @Override
            public void manualAddChainNode(ITrackedPager pager, boolean report) {
                if (pager instanceof Fragment) {
                    Activity activity = ((Fragment) pager).getActivity();
//                    Fragment parentFg = ((Fragment) pager).getParentFragment();
//                    if (parentFg != null) {
//                        Log.i(PagerChainTracker.tag, "a fragment's sub fragment should not use REPLACE,还没有想过这种如何设计", new Throwable());
//                        super.manualAddChainNode(pager, report);
//                    } else
                    if (activity instanceof ITrackedPager) {
                        PagerChainTracker.Companion.replacePagerInfo((ITrackedPager) activity, pager, report);
                    } else
                        super.manualAddChainNode(pager, report);
                } else
                    super.manualAddChainNode(pager, report);
            }
        },

//        /**
//         * 作为宿主点的一部分信息，暂时没有使用场景，目前走手动塞入信息即可
//         */
//        INJECT,

        /**
         * 作为一个独立的新点
         */
        ALONE;

        public void manualAddChainNode(ITrackedPager pager, boolean report) {
            PagerChainTracker.Companion.manualAddChainNode(PagerChainTracker.Companion.createPagerEntity(pager), report);
        }
    }
}
