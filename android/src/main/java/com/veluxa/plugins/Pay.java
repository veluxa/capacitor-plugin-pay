package com.veluxa.plugins;

import android.Manifest;
import android.app.Activity;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import com.alipay.sdk.app.PayTask;
import com.getcapacitor.Bridge;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.veluxa.plugins.aliapi.Alipay;

import java.util.Map;

@NativePlugin
public class Pay extends Plugin {

    public static final String ERROR_INVALID_PARAMETERS = "参数格式错误";
    public static final String ERROR_WECHAT_NOT_INSTALLED = "未安装微信";
    public static final String ERROR_SEND_REQUEST_FAILED = "发送请求失败";
    public final static int REQUEST_READ_PHONE_STATE = 1;
    static PluginCall _call;
    static Bridge bridge;
    static Alipay alipay = new Alipay();

    //获取权限
    private void requestPermissions() {
        if (!this.hasPermission(Manifest.permission.READ_PHONE_STATE)) {
            this.pluginRequestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }
    }

    public static PluginCall getCall() { return _call; }

    public static String getWxAppId() {
        return bridge.getConfig().getString("wxappId");
    }

    @PluginMethod
    public void wxPayRequest(PluginCall call) {
        _call = call;
        bridge = this.getBridge();
//        requestPermissions();
        String appId = getWxAppId(); //appid
        PayReq req = new PayReq();
        req.appId = appId;
        req.partnerId = call.getString("mch_id"); // 商户号
        req.prepayId = call.getString("prepay_id"); // 预支付交易会话标识
        req.nonceStr = call.getString("nonce"); // 随机字符串
        req.timeStamp = call.getString("timestamp"); // 时间戳
        req.sign = call.getString("sign"); // 签名
        req.packageValue = "Sign=WXPay"; // 签名

        saveCall(call);
        if(!req.checkArgs()) {
            call.reject(ERROR_INVALID_PARAMETERS,"-7");
            return;
        }

        final IWXAPI api = WXAPIFactory.createWXAPI(this.getContext(),appId,true);
        if(!api.isWXAppInstalled()) {
            call.reject(ERROR_WECHAT_NOT_INSTALLED,"-7");
            return;
        }

        api.registerApp(appId);
        if(!api.sendReq(req)) {
            call.reject(ERROR_SEND_REQUEST_FAILED,"-7");
        }

    }

    @PluginMethod
    public void aliPayRequest(final PluginCall call) {
        _call = call;
        final String orderInfo = call.getString("orderInfo");
        final Activity act = getBridge().getActivity();
        saveCall(call);

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask task = new PayTask(act);
                Map<String,String> result = task.payV2(orderInfo,true);

                Message msg = new Message();
                msg.what = alipay.SDK_PAY_FLAG;
                msg.obj = result;

                alipay.mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
