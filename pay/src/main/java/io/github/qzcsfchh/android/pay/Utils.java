package io.github.qzcsfchh.android.pay;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

/**
 * Desc.
 *
 * @author huanghao
 * @version v1.0
 * @since 2021/5/20 01:39
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class Utils {
    public static final String WECHAT_APP_ID = "wechat_app_id";


    public static void runOnMainThread(@NonNull Runnable runnable){
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public static void runOnThread(@NonNull Runnable runnable){
        AsyncTask.SERIAL_EXECUTOR.execute(runnable);  //这里推荐用SERIAL_EXECUTOR因为支付我们app支付不可能并发
    }

    @NonNull
    public static String readMetaValue(Context context, String name){
        if (context == null || name == null) {
            return "";
        }
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object value = null;
            if (info != null && (value = info.metaData.get(name))!=null) {
                if (value instanceof String) {
                    return (String) value;
                } else if (value instanceof Integer) {
                    return String.valueOf(value);
                } else {
                    return value.toString();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static boolean hasInstalled(Context context,String packageName){
        if (TextUtils.isEmpty(packageName)) return false;
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)!=null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


}
