package osp.leobert.android.tracker;

import android.util.Pair;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * 业务脱敏，大部分内容已被移除
 * Created by leobert on 2018/12/19.
 */
public abstract class BuryPoint {

    public static BuryPoint normal(@NonNull String key) {
        return new NormalBuryPoint(key);
    }

    public static BuryPoint transfer(@NonNull String key) {
        return new TransferBuryPoint(key);
    }

    @NonNull
    private String key;

    @NonNull
    public String getKey() {
        return key;
    }

    BuryPoint(@NonNull String key) {
        this.key = key;
    }

    void handle(@NonNull BuryPointContext context, Pair<String, String>[] appendData) {
    }

    void handle(@NonNull BuryPointContext context, List<Pair<String, String>> appendData) {

    }

    void allocate(@NonNull BuryPointContext context, @NonNull List<Pair<String, String>> appendData) {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuryPoint buryPoint = (BuryPoint) o;

        return key.equals(buryPoint.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    /**
     * 普通点，不会派生其他点
     */
    public static class NormalBuryPoint extends BuryPoint {
        NormalBuryPoint(@NonNull String key) {
            super(key);
        }
    }

    /**
     * 本身点名无法确定，key只是一个事件唯一标识，根据上下文处理成真正的点
     */
    public static class TransferBuryPoint extends BuryPoint {

        TransferBuryPoint(@NonNull String key) {
            super(key);
        }

        @Override
        void handle(@NonNull BuryPointContext context, Pair<String, String>[] appendData) {
        }

        void handle(@NonNull BuryPointContext context, List<Pair<String, String>> appendData) {
        }

        @Override
        void allocate(@NonNull BuryPointContext context, @NonNull List<Pair<String, String>> appendData) {
            super.allocate(context, appendData);
        }
    }

    /**
     * 可派生的点，慎用，目前我能想到的场景都可以使用TransferBuryPoint 构建出多个NormalBuryPoint处理
     * //     * @hide
     */
    private static class FissileBuryPoint extends BuryPoint {

        public FissileBuryPoint(@NonNull String key) {
            super(key);
        }
    }

}
