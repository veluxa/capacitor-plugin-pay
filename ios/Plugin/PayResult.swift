//
//  PayResult.swift
//  Plugin
//
//  Created by Max on 2020/8/31.
//  Copyright © 2020 Max Lynch. All rights reserved.
//

import Foundation

typealias JSObject = [String:Any]
let call = CAPPluginCall();
var ret = JSObject()
    
    
    var ERROR_WECHAT_NOT_INSTALLED = "未安装微信";
    var ERROR_SEND_REQUEST_FAILED = "发送请求失败";
    var ALIPAY_RESPONSE_PAY_OK = "支付成功";
    var ERROR_ALIPAY_RESPONSE_PAY_FAIL = "支付失败";
    var ERROR_ALIPAY_RESPONSE_ORDER_PROCESSING = "订单正在处理中";
    var ERROR_ALIPAY_RESPONSE_SEND_REPEAT = "重复请求";
    var ERROR_ALIPAY_RESPONSE_USER_CANCEL = "中途取消";
    var ERROR_ALIPAY_RESPONSE_NETWORK_UNAVAILABLE = "网络连接出错";
    var ERROR_ALIPAY_RESPONSE_PAY_UNKNOWN = "支付结果未知";
    
    var WECHAT_RESPONSE_OK = "操作成功";
    var ERROR_WECHAT_RESPONSE_COMMON = "签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、微信账号异常等。";
    var ERROR_WECHAT_RESPONSE_USER_CANCEL = "用户点击取消并返回";
    var ERROR_WECHAT_RESPONSE_SENT_FAILED = "发送失败";
    var ERROR_WECHAT_RESPONSE_AUTH_DENIED = "授权失败";
    var ERROR_WECHAT_RESPONSE_UNSUPPORT = "微信不支持";
    var ERROR_WECHAT_RESPONSE_UNKNOWN = "未知错误";
    
    public func WechatCall(_ code: WXErrCode) {
        print("进入wechatcall_1")
        
        switch code {
        case WXSuccess:
            ret["message"] = WECHAT_RESPONSE_OK;
            ret["code"] = "0";
            call.resolve(ret);
            break;
        case WXErrCodeCommon:
            call.reject(ERROR_WECHAT_RESPONSE_COMMON,"-1");
            break;
        case WXErrCodeUserCancel:
            call.reject(ERROR_WECHAT_RESPONSE_USER_CANCEL,"-2");
            break;
        case WXErrCodeSentFail:
            call.reject(ERROR_WECHAT_RESPONSE_SENT_FAILED,"-3");
            break;
        case WXErrCodeAuthDeny:
            call.reject(ERROR_WECHAT_RESPONSE_AUTH_DENIED,"-4");
            break;
        case WXErrCodeUnsupport:
            call.reject(ERROR_WECHAT_RESPONSE_UNSUPPORT,"-5");
            break;
        default:
            call.reject(ERROR_WECHAT_RESPONSE_UNKNOWN,"-6");
            break;
        }
    }
    
    public func WechatCall(_ code: String) {
        print("进入wechatcall_2")

        switch code {
        case "-8":
            call.reject(ERROR_WECHAT_NOT_INSTALLED,"-8");
            break;
        case "-9":
            call.reject(ERROR_SEND_REQUEST_FAILED,"-9");
            break;
        default:
            call.reject(ERROR_WECHAT_RESPONSE_UNKNOWN,"-6");
            break;
        }
    }
    
    public func AlipayCall(_ code: String) {
        print("进入AlipayCall")

        switch code {
        case "9000":
            ret["message"] = ALIPAY_RESPONSE_PAY_OK;
            ret["code"] = "9000";
            call.resolve(ret);
        case "8000":
            call.reject(ERROR_ALIPAY_RESPONSE_ORDER_PROCESSING,"8000");
            break
        case "4000":
            call.reject(ERROR_ALIPAY_RESPONSE_PAY_FAIL,"4000");
            break
        case "5000":
            call.reject(ERROR_ALIPAY_RESPONSE_SEND_REPEAT,"5000");
            break
        case "6001":
            call.reject(ERROR_ALIPAY_RESPONSE_USER_CANCEL,"6001");
            break
        case "6002":
            call.reject(ERROR_ALIPAY_RESPONSE_NETWORK_UNAVAILABLE,"6002");
            break
        case "6004":
            call.reject(ERROR_ALIPAY_RESPONSE_PAY_UNKNOWN,"6004");
            break
        default:
            call.reject(ERROR_ALIPAY_RESPONSE_PAY_FAIL,"4000");
            break
        }
    }

