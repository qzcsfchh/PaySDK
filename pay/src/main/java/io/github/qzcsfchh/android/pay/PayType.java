package io.github.qzcsfchh.android.pay;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>支付渠道</p>
 *
 * @author huanghao
 * @version v1.0
 * @since 2021/5/20 01:39
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({PayType.AliPay, PayType.WxPay, PayType.UpPay, PayType.UpPhonePay})
public @interface PayType {
    int AliPay = 1;
    int WxPay = 2;
    int UpPay = 3;
    int UpPhonePay = 4;
}
