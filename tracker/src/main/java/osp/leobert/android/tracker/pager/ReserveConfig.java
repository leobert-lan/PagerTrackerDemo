package osp.leobert.android.tracker.pager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p><b>Package:</b> osp.leobert.android.tracker.pager </p>
 * <p><b>Classname:</b> ReserveConfig </p>
 * Created by leobert on 2020/5/14.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ReserveConfig {
    String on();

    String asPoint();
}
