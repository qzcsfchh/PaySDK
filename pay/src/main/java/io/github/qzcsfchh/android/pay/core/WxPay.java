package io.github.qzcsfchh.android.pay.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import io.github.qzcsfchh.android.pay.Utils;

/**
 * 微信支付，app_id从平台获取
 *
 * @author huanghao
 * @version v1.0
 * @since 2021/5/20 01:39
 */
public class WxPay implements IPay{
    private static final String TAG = "WxPay";
    private final IWXAPI iwxapi;
    private final Context context;
    private final Gson gson = new Gson();

    public WxPay(Context context) {
        this.context = context.getApplicationContext();
        // TODO: 2021/5/12 确认下这里是否需要appid
        final String appid = Utils.readMetaValue(context, Utils.WECHAT_APP_ID);
        iwxapi = WXAPIFactory.createWXAPI(context, null, false);
        boolean b = iwxapi.registerApp(appid);
    }

    @Override
    public boolean isAppInstalled() {
        return iwxapi.isWXAppInstalled();
    }

    @Override
    public boolean isSupportH5() {
        return false;
    }

    @Override
    public String getAppName() {
        return "微信";
    }

    @Override
    public void pay(@NonNull String arguments, @NonNull PayListener listener) {
        final WxPayInfo info;
        try {
            info = gson.fromJson(arguments, WxPayInfo.class);
        } catch (JsonSyntaxException e) {
            listener.onPayFail(-1, "支付参数异常");
            return;
        }
        PayReq payReq = new PayReq();
        payReq.appId = info.getAppid();
        payReq.partnerId = info.getMch_id();
        payReq.prepayId = info.getPrepay_id();
        payReq.packageValue = "Sign=WXPay";//info.getPackageX() == null ? "Sign=WXPay" : info.getPackageX();
        payReq.nonceStr = info.getNonce_str();
        payReq.timeStamp = info.getTimestamp();
        payReq.sign = info.getSign();
        if (!payReq.checkArgs()) {
            listener.onPayFail(-1,"支付参数不完整");
            return;
        }
        if (!iwxapi.sendReq(payReq)) {
            listener.onPayFail(-1,"微信支付异常");
            return;
        }
        InnerListener.getInstance().setPayListener(listener);
    }

    public static final class WxPayInfo {

        /**
         * nonce_str : WIYJIQJKYd8ocC1x
         * package : WXPay
         * appid : wxaaad5d7f30b5268a
         * sign : D2799462D486BAEA1EBD815366358A4B
         * trade_type : APP
         * return_msg : OK
         * result_code : SUCCESS
         * mch_id : 1333427601
         * return_code : SUCCESS
         * prepay_id : wx28155229679122adc9ca08f11635397633
         * timestamp : 1556437948
         */
        @SerializedName(value = "nonce_str",alternate = {"noncestr"})
        private String nonce_str;
        @SerializedName("package")
        private String packageX;
        @SerializedName("appid")
        private String appid;
        @SerializedName("sign")
        private String sign;
        @SerializedName("trade_type")
        private String trade_type;
        @SerializedName("return_msg")
        private String return_msg;
        @SerializedName("result_code")
        private String result_code;
        @SerializedName(value = "mch_id",alternate = {"partnerid"})
        private String mch_id;  //对应partnerId，很关键
        @SerializedName("return_code")
        private String return_code;
        @SerializedName(value = "prepay_id",alternate = {"prepayid"})
        private String prepay_id;
        @SerializedName("timestamp")
        private String timestamp;

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }

        public String getReturn_msg() {
            return return_msg;
        }

        public void setReturn_msg(String return_msg) {
            this.return_msg = return_msg;
        }

        public String getResult_code() {
            return result_code;
        }

        public void setResult_code(String result_code) {
            this.result_code = result_code;
        }

        public String getMch_id() {
            return mch_id;
        }

        public void setMch_id(String mch_id) {
            this.mch_id = mch_id;
        }

        public String getReturn_code() {
            return return_code;
        }

        public void setReturn_code(String return_code) {
            this.return_code = return_code;
        }

        public String getPrepay_id() {
            return prepay_id;
        }

        public void setPrepay_id(String prepay_id) {
            this.prepay_id = prepay_id;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "WxPayInfo{" + "nonce_str='" + nonce_str + '\'' + ", packageX='" + packageX + '\'' + ", appid='" + appid + '\'' + ", sign='" + sign + '\'' + ", trade_type='" + trade_type + '\'' + ", return_msg='" + return_msg + '\'' + ", result_code='" + result_code + '\'' + ", mch_id='" + mch_id + '\'' + ", return_code='" + return_code + '\'' + ", prepay_id='" + prepay_id + '\'' + ", timestamp='" + timestamp + '\'' + '}';
        }
    }
}
