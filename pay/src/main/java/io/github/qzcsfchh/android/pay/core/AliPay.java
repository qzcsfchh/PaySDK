package io.github.qzcsfchh.android.pay.core;

import android.app.Activity;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.alipay.sdk.app.PayTask;

import java.lang.ref.WeakReference;
import java.util.Map;

import io.github.qzcsfchh.android.pay.Utils;

/**
 * <p>支付宝支付，订单生成及签名交给后端去做</p>
 *
 * @author huanghao
 * @version v1.0
 * @since 2021/5/20 01:39
 */
public class AliPay implements IPay {
    private WeakReference<Activity> refActivity;
    public static final String ALIPAY_PACKAGE_NAME = "com.eg.android.AlipayGphone";

    public AliPay(Activity activity) {
        this.refActivity = new WeakReference<>(activity);
    }

    @Override
    public boolean isAppInstalled() {
        return refActivity.get() != null && Utils.hasInstalled(refActivity.get(), ALIPAY_PACKAGE_NAME);
    }

    @Override
    public boolean isSupportH5() {
        return true;
    }

    @Override
    public String getAppName() {
        return "支付宝";
    }

    /**
     * 调用支付宝SDK支付接口
     * <pre>
     * result字段示例：
     * {
     * "alipay_trade_app_pay_response": {
     *      "code": "10000",
     *      "msg": "Success",
     *      "app_id": "2015011300025068",
     *      "auth_app_id": "2015011300025068",
     *      "charset": "utf-8",
     *      "timestamp": "2019-04-29 11:20:31",
     *      "out_trade_no": "5e143ac1-bf98-4955-95f2-006a60bd41d8",
     *      "total_amount": "0.01",
     *      "trade_no": "2019042922001462901042173422",
     *      "seller_id": "2088811103610681"
     * },
     * "sign": "mVav4U+tEk4024ky6oY32Z4vD41alpsNyxYvVP2pQalCYmZqYH4gdDlLJLs5a0l+hw+7IH1CECeg80CHvkf8txn63QeYaY2+mRtCMwWX+6I9sNZu3+gvsJKa0EMk7tU1E0f4KdEyj5tsh4VgXshQfVx/XYuYtQW8eSX3SVgyvFQ1p4xPv0A8yIPg6sWzmSoe0Hh3vu1xNYy/ttQ1PzigZRsgUSwwzpcxoxGuoS9DTYaYzhnM6gF1oDeZsmrMcYozSLDXcUcxk9E260A9+xi2Pi3+eRJrxzZ0zHCOxx2L1bBoPqHLZqAFwsUZ5gOHFxqU2A1h+Q4HFWMuKwqCCHg10A==",
     * "sign_type": "RSA2"
     * }
     * </pre>
     * @param signedPayInfo
     * @param listener
     */
    @Override
    public void pay(@NonNull String signedPayInfo, @NonNull PayListener listener) {
        final Activity activity = refActivity.get();
        if (activity == null) return;
        Utils.runOnThread(() -> {
            PayTask payTask = new PayTask(activity);
            final Map<String, String> map = payTask.payV2(signedPayInfo, true);
            Utils.runOnMainThread(()->{
                if (map == null) {
                    listener.onPayFail(-1,"支付宝异常，支付失败");
                    return;
                }
                String resultStatus = map.get("resultStatus");
                String result = map.get("result");
                String memo = map.get("memo");
                if (TextUtils.equals(resultStatus, "9000")) {
                    listener.onPaySuccess(result);
                } else {
                    listener.onPayFail(-1, memo == null ? "支付失败" : memo);
                }
            });
        });
    }

}
