package osp.leobert.android.tracker.pager;

import androidx.annotation.NonNull;

/**
 * <p><b>Package:</b> osp.leobert.android.tracker.pager </p>
 * <p><b>Classname:</b> ITrackedPager </p>
 * Created by leobert on 2020/5/14.
 */
public interface ITrackedPager {
    void setPagerToken(@NonNull String pagerToken);

    @NonNull
    String getPagerToken();

    interface FragmentInViewPager extends ITrackedPager{
    }
}
