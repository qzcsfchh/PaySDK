package io.github.qzcsfchh.android.pay.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.github.qzcsfchh.android.pay.Pay;
import io.github.qzcsfchh.android.pay.PayType;
import io.github.qzcsfchh.android.pay.core.PayListener;
import io.github.qzcsfchh.android.pay .core.WxPay;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pay.init(this);
        setContentView(R.layout.activity_main);
    }


    public void onAlipay(View view) {
        String payInfo = "alipay_sdk=alipay-sdk-java-3.7.4.ALL&" +
                "app_id=2019100868000000&" +
                "biz_content=%7B%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%225e143ac1-bf98-4955-95f2-006a60bd41d8%22%2C%22passback_params%22%3A%22%E8%BF%99%E9%87%8C%E6%9C%89%E4%B8%AA%E5%B0%8F%E5%8C%BA%E7%BC%96%E5%8F%B7%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22App%E6%94%AF%E4%BB%98%E6%B5%8B%E8%AF%95Java%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&" +
                "charset=utf-8&" +
                "format=json&" +
                "method=alipay.trade.app.pay&" +
                "notify_url=http%3A%2F%2F115.236.50.12%3A16132%2Fpay%2Fui%2Ftest%2Fnotify&" +
                "sign=eQb000000004Xud0+UVQll0000M+tBIIKkpf1k/H8rsnlalhs400svM9hqF//Fhb3Ic1Fk9Ii0oSv9g==&" +
                "sign_type=RSA2&" +
                "timestamp=2020-06-29+11%3A19%3A08&" +
                "version=1.0";
        Pay.with(this).type(PayType.AliPay).arguments(payInfo).pay(new PayListener() {
            @Override
            public void onPaySuccess(@NonNull String result) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayFail(int code, @NonNull String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onWxPay(View view) {
        String payInfo = "{\"nonce_str\":\"42tKqSoz7sdhfvoE\",\"package\":\"Sign=WXPay\",\"appid\":\"wxaaad5d7f30b5268a\",\"sign\":\"30977A8079D93943D03E744F086F9487\",\"trade_type\":\"APP\",\"return_msg\":\"OK\",\"result_code\":\"SUCCESS\",\"mch_id\":\"1333427601\",\"return_code\":\"SUCCESS\",\"prepay_id\":\"wx28212305428035a9e97893002001586256\",\"timestamp\":\"1556457786\"}\n";
        Pay.with(this).type(PayType.WxPay).arguments(payInfo).pay(new PayListener() {
            @Override
            public void onPaySuccess(@NonNull String result) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayFail(int code, @NonNull String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void createSign(WxPay.WxPayInfo wxPayInfo){
        if (wxPayInfo == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("appid=").append(wxPayInfo.getAppid())
                .append("&").append("noncestr=").append(wxPayInfo.getNonce_str())
                .append("&").append("package=").append(wxPayInfo.getPackageX())
                .append("&").append("noncestr=").append(wxPayInfo.getNonce_str())
                .append("&").append("partnerid=").append(wxPayInfo.getMch_id())
                .append("&").append("prepayid=").append(wxPayInfo.getPrepay_id())
                .append("&").append("timestamp=").append(wxPayInfo.getTimestamp())
                .append("&").append("key=").append("wxappkey");
        try {
            byte[] md5s = MessageDigest.getInstance("MD5").digest(sb.toString().getBytes());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void onUPPay(View view) {
        String payInfo = "{}";
        Pay.with(this).type(PayType.UpPay).arguments(payInfo).pay(new PayListener() {
            @Override
            public void onPaySuccess(@NonNull String result) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayFail(int code, @NonNull String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onUPPhonePay(View view) {
        String payInfo = "{}";
        Pay.with(this).type(PayType.UpPhonePay).arguments(payInfo).pay(new PayListener() {
            @Override
            public void onPaySuccess(@NonNull String result) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayFail(int code, @NonNull String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}