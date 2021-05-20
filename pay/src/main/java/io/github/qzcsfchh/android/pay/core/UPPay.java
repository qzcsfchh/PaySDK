package io.github.qzcsfchh.android.pay.core;

import android.content.Context;

import androidx.annotation.NonNull;

import io.github.qzcsfchh.android.pay.up.UPPayEntry;

/**
 * <p>银联普通支付</p>
 *
 * @author huanghao
 * @version v1.0
 * @since 2021/5/20 01:39
 */
public class UPPay implements IPay{
    private final Context mContext;

    public UPPay(Context context) {
        mContext = context;
    }

    @Override
    public boolean isAppInstalled() {
        return true;
    }

    @Override
    public boolean isSupportH5() {
        return true;
    }

    @Override
    public String getAppName() {
        return "云闪付";
    }

    @Override
    public void pay(@NonNull String arguments, @NonNull PayListener listener) {
        UPPayEntry.startPay(mContext, arguments);
        InnerListener.getInstance().setPayListener(listener);
    }

}
