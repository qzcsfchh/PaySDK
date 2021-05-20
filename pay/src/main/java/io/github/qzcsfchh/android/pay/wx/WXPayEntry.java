package io.github.qzcsfchh.android.pay.wx;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import io.github.qzcsfchh.android.pay.core.InnerListener;

/**
 * Desc.
 *
 * @author huanghao
 * @version v1.0
 * @since 2021/5/20 01:39
 */
public class WXPayEntry extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
	private final Gson gson = new Gson();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	api = WXAPIFactory.createWXAPI(this, null);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType()!= ConstantsAPI.COMMAND_PAY_BY_WX) {
			finish();
			return;
		}
		InnerListener listener = InnerListener.getInstance();
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK://支付成功，显示充值成功的页面和需要的操作
				String result = gson.toJson(resp);
				listener.onPaySuccess(result);
				break;
			case BaseResp.ErrCode.ERR_COMM://错误,可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等
				listener.onPayFail(BaseResp.ErrCode.ERR_COMM, resp.errStr == null ? "支付失败" : resp.errStr);
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消:无需处理。发生场景：用户不支付了，点击取消，返回APP
				listener.onPayFail(BaseResp.ErrCode.ERR_USER_CANCEL, resp.errStr == null ? "取消支付" : resp.errStr);
				break;
			case BaseResp.ErrCode.ERR_SENT_FAILED:
				listener.onPayFail(BaseResp.ErrCode.ERR_SENT_FAILED, resp.errStr == null ? "请求微信支付失败" : resp.errStr);
				break;
			case BaseResp.ErrCode.ERR_UNSUPPORT:
				listener.onPayFail(BaseResp.ErrCode.ERR_UNSUPPORT, resp.errStr == null ? "不支持微信支付" : resp.errStr);
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				listener.onPayFail(BaseResp.ErrCode.ERR_AUTH_DENIED, resp.errStr == null ? "支付授权失败" : resp.errStr);
				break;
			case BaseResp.ErrCode.ERR_BAN:
				listener.onPayFail(BaseResp.ErrCode.ERR_BAN, resp.errStr == null ? "支付被禁用" : resp.errStr);
				break;
			default:
				listener.onPayFail(resp.errCode, resp.errStr == null ? "支付失败" : resp.errStr);
				break;
		}
		finish();
	}





}