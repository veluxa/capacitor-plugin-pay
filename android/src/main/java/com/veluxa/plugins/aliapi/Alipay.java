package com.veluxa.plugins.aliapi;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.getcapacitor.JSObject;

import java.util.Map;

import com.getcapacitor.PluginCall;
import com.veluxa.plugins.Pay;

public class Alipay {

    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_AUTH_FLAG = 2;
    public static final String AliPAY_RESPONSE_OK = "操作成功";
    public static final String ERROR_ALIPAY_RESPONSE_COMMON = "操作失败";

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            PluginCall call = Pay.getCall();
            JSObject ret = new JSObject();

            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ret.put("code", 0);
                        ret.put("message",AliPAY_RESPONSE_OK);
                        call.resolve(ret);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        call.reject(ERROR_ALIPAY_RESPONSE_COMMON,"-1");
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        ret.put("code", 0);
                        ret.put("message",AliPAY_RESPONSE_OK);
                        call.resolve(ret);
                    } else {
                        // 其他状态值则为授权失败
                        call.reject(ERROR_ALIPAY_RESPONSE_COMMON,"-1");
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
}
