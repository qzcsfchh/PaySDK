package io.github.qzcsfchh.android.pay.core;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.unionpay.UPPayAssistEx;
import com.unionpay.UPQuerySEPayInfoCallback;

import io.github.qzcsfchh.android.pay.up.UPPayEntry;

/**
 * 银联手机支付，属于手机厂商支付
 *
 * @author huanghao
 * @version v1.0
 * @since 2021/5/20 01:39
 */
public class UPPhonePay implements IPay{
    private static final String TAG = "UPPhonePay";
    private static String seName, seType;
    private static boolean enabled;
    private final Context context;

    public UPPhonePay(Context context) {
        this.context = context;
    }

    @Override
    public boolean isAppInstalled() {
        return enabled && seType != null;
    }

    @Override
    public boolean isSupportH5() {
        return false;
    }

    @Override
    public String getAppName() {
        return seName == null ? "手机支付" : seName;
    }

    @Override
    public void pay(@NonNull String arguments, @NonNull PayListener listener) {
        UPPayEntry.startSEPay(context, arguments, seType);
        InnerListener.getInstance().setPayListener(listener);
    }

    public static void getSEPayInfo(Context context){
        UPPayAssistEx.getSEPayInfo(context, new UPQuerySEPayInfoCallback() {
            @Override
            public void onResult(String seName, String seType, int cardNumbers, Bundle bundle) {
                Log.d(TAG, "onResult(): seName = [" + seName + "], seType = [" + seType + "], cardNumbers = [" + cardNumbers + "], bundle = [" + bundle + "]");
                UPPhonePay.seName = seName;
                UPPhonePay.seType = seType;
                enabled = true;
            }

            @Override
            public void onError(String seName, String seType, String errorCode, String msg) {
                Log.d(TAG, "onError(): seName = [" + seName + "], seType = [" + seType + "], errorCode = [" + errorCode + "], msg = [" + msg + "]");
                enabled = false;
            }
        });
    }
}
