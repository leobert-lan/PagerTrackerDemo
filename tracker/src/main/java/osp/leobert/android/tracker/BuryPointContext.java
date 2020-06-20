package osp.leobert.android.tracker;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * 业务脱敏，大部分内容已被移除
 * Created by leobert on 2018/12/18.
 */
public abstract class BuryPointContext {


    public void track(String pointKey) {

    }

    @SafeVarargs
    public final void track(String pointKey, Pair<String, String>... appendData) {

    }


    @SafeVarargs
    public final void track(@NonNull BuryPoint point, Pair<String, String>... appendData) {

    }

    @SafeVarargs
    public final void track(@NonNull BuryPoint point, boolean useParent, Pair<String, String>... appendData) {
    }

    public final void track(@NonNull BuryPoint point, boolean useParent, @NonNull List<Pair<String, String>> appendData) {
    }


    @Nullable
    public static BuryPointUploader buryPointUploader;

    public interface BuryPointUploader {
        void upload(String pointKey, List<Pair<String, String>> params);
    }
}
