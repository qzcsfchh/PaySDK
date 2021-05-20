package io.github.qzcsfchh.android.pay.core;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

/**
 * 内部回调工具.
 *
 * @author huanghao
 * @version v1.0
 * @since 2021/5/20 01:39
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class InnerListener implements PayListener {
    private PayListener payListener;

    private static final class Holder {
        private static final InnerListener INSTANCE = new InnerListener();
    }

    private InnerListener() {
    }

    public static InnerListener getInstance() {
        return Holder.INSTANCE;
    }

    public void setPayListener(PayListener listener) {
        this.payListener = listener;
    }

    @Override
    public void onPaySuccess(@NonNull String result) {
        if (payListener != null) {
            payListener.onPaySuccess(result);
            payListener = null;
        }
    }

    @Override
    public void onPayFail(int code, @NonNull String error) {
        if (payListener != null) {
            payListener.onPayFail(code, error);
            payListener = null;
        }
    }
}
