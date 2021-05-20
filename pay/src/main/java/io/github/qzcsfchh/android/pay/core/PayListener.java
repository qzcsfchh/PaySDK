package io.github.qzcsfchh.android.pay.core;

import androidx.annotation.NonNull;

/**
 * <p>支付回调</p>
 *
 * @author huanghao
 * @version v1.0
 * @since 2021/5/20 01:39
 */
public interface PayListener{

    /**
     * On pay success.
     *
     * @param result 支付结果，微信支付结果没什么用，支付宝支付结果用于验签
     * @author huanghao6 Created on 2019-04-29
     */
    void onPaySuccess(@NonNull String result);

    /**
     * On pay fail.
     *
     * @param code  错误码
     * @param error 错误信息
     * @author huanghao6 Created on 2019-04-29
     */
    void onPayFail(int code, @NonNull String error);
}
