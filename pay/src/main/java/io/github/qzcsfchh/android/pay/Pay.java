package io.github.qzcsfchh.android.pay;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Locale;

import io.github.qzcsfchh.android.pay.core.AliPay;
import io.github.qzcsfchh.android.pay.core.IPay;
import io.github.qzcsfchh.android.pay.core.PayListener;
import io.github.qzcsfchh.android.pay.core.UPPay;
import io.github.qzcsfchh.android.pay.core.UPPhonePay;
import io.github.qzcsfchh.android.pay.core.WxPay;

/**
 * <p>支付入口</p>
 *
 * @author huanghao
 * @version v1.0
 * @since 2021/5/20 01:39
 */
public class Pay {
    private final Activity mActivity;
    @PayType
    private int mPayChannel;
    private String mPayArguments;
    private static volatile boolean inited;

    public static void init(Context context) {
        if (inited) return;
        UPPhonePay.getSEPayInfo(context);
        inited = true;
    }

    public static Pay with(Activity activity) {
        return new Pay(activity);
    }

    private Pay(Activity activity) {
        mActivity = activity;
    }

    public Pay type(@PayType int type) {
        mPayChannel = type;
        return this;
    }

    public Pay arguments(@Nullable String payArguments) {
        mPayArguments = payArguments;
        return this;
    }

    public void pay(@NonNull PayListener listener) {
        if (TextUtils.isEmpty(mPayArguments)) {
            listener.onPayFail(-1, "支付参数缺失");
            return;
        }
        IPay pay;
        switch (mPayChannel) {
            case PayType.WxPay:
                pay = new WxPay(mActivity.getApplicationContext());
                break;
            case PayType.AliPay:
                pay = new AliPay(mActivity);
                break;
            case PayType.UpPay:
                pay = new UPPay(mActivity);
                break;
            case PayType.UpPhonePay:
                pay = new UPPhonePay(mActivity);
                break;
            default:
                throw new RuntimeException("Must specify a pay channel.");
        }
        if (!pay.isSupportH5() && !pay.isAppInstalled()) {
            listener.onPayFail(-1, String.format(Locale.getDefault(), "请先安装%s", pay.getAppName()));
            return;
        }
        pay.pay(mPayArguments, listener);
    }
}
