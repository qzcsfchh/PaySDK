package io.github.qzcsfchh.android.pay.core;

import androidx.annotation.NonNull;

/**
 * 支付接口.
 *
 * @author huanghao
 * @version v1.0
 * @since 2021/5/20 01:39
 */
public interface IPay {

    boolean isAppInstalled();

    boolean isSupportH5();

    String getAppName();

    void pay(@NonNull String arguments, @NonNull PayListener listener);
}
