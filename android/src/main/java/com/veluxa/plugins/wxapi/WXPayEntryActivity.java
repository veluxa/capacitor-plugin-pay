package com.veluxa.plugins.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.veluxa.plugins.Pay;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    public static final String TAG = "Plugin.WechatPay";
    protected static IWXAPI wxAPI;
    private IWXAPI api;
    protected String appId;

    public static final String WECHAT_RESPONSE_OK = "操作成功";
    public static final String ERROR_WECHAT_RESPONSE_COMMON = "签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、微信账号异常等。";
    public static final String ERROR_WECHAT_RESPONSE_USER_CANCEL = "用户点击取消并返回";
    public static final String ERROR_WECHAT_RESPONSE_SENT_FAILED = "发送失败";
    public static final String ERROR_WECHAT_RESPONSE_AUTH_DENIED = "授权失败";
    public static final String ERROR_WECHAT_RESPONSE_UNSUPPORT = "微信不支持";
    public static final String ERROR_WECHAT_RESPONSE_UNKNOWN = "未知错误";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = getWxAPI(this);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    public IWXAPI getWxAPI(Context ctx) {
        if(wxAPI == null) {
            appId = Pay.getWxAppId();
            return WXAPIFactory.createWXAPI(ctx,appId,false);
        }
        return wxAPI;
    }

    @Override
    public void onResp(BaseResp resp) {
        PluginCall call = Pay.getCall();

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                JSObject ret = new JSObject();
                ret.put("code",0);
                ret.put("message",WECHAT_RESPONSE_OK);
                call.resolve(ret);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                call.reject(ERROR_WECHAT_RESPONSE_USER_CANCEL,"-2");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                call.reject(ERROR_WECHAT_RESPONSE_AUTH_DENIED,"-4");
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                call.reject(ERROR_WECHAT_RESPONSE_SENT_FAILED,"-3");
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                call.reject(ERROR_WECHAT_RESPONSE_UNSUPPORT,"-5");
                break;
            case BaseResp.ErrCode.ERR_COMM:
                call.reject(ERROR_WECHAT_RESPONSE_COMMON,"-1");
                break;
            default:
                call.reject(ERROR_WECHAT_RESPONSE_UNKNOWN,"-6");
                break;
        }

        finish();
    }

    @Override
    public void onReq(BaseReq req) {
        finish();
    }
}
