package io.github.qzcsfchh.android.pay.up;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.unionpay.UPPayAssistEx;

import io.github.qzcsfchh.android.pay.core.InnerListener;

/**
 * <p>云闪付入口</p>
 *
 * @author huanghao
 * @version v1.0
 * @since 2021/5/20 01:39
 */
public class UPPayEntry extends Activity {
    private static final String TAG = "UPPayEntry";
    public static final String EXTRA_TN = "extra_tn";
    public static final String EXTRA_SETYPE = "extra_setype";
    /*****************************************************************
     * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境（测试环境不发生真实交易）
     *****************************************************************/
    private final String mMode = "00";

    public static void startPay(Context context, @NonNull String tn) {
        context.startActivity(new Intent(context, UPPayEntry.class).putExtra(EXTRA_TN, tn));
    }

    public static void startSEPay(Context context, @NonNull String tn, @NonNull String seType) {
        context.startActivity(new Intent(context, UPPayEntry.class).putExtra(EXTRA_TN, tn).putExtra(EXTRA_SETYPE, seType));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String spId = null;
        String sysProvider = null;
        String orderInfo = getIntent().getStringExtra(EXTRA_TN);
        String seType = getIntent().getStringExtra(EXTRA_SETYPE);
        if (seType == null) {
            UPPayAssistEx.startPay(this, spId, sysProvider, orderInfo, mMode);
        } else {
            UPPayAssistEx.startSEPay(this, spId, sysProvider, orderInfo, mMode, seType);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final InnerListener listener = InnerListener.getInstance();
        if (data == null) {
            listener.onPayFail(-1,"支付结果异常");
        }else {
            String pay_result = data.getStringExtra("pay_result");
            // 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
            if ("success".equalsIgnoreCase(pay_result)) {
                // 如果想对结果数据验签，可使用下面这段代码，但建议不验签，直接去商户后台查询交易结果
                String result_data = data.getStringExtra("result_data");
                listener.onPaySuccess(result_data == null ? "" : result_data);
            } else if ("fail".equalsIgnoreCase(pay_result)) {
                listener.onPayFail(-2, "支付失败");
            } else if ("cancel".equalsIgnoreCase(pay_result)) {
                listener.onPayFail(-3, "取消支付");
            } else {
                listener.onPayFail(-1, "支付结果异常");
            }
        }
        finish();
    }
}
