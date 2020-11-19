import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(Pay)
public class Pay: CAPPlugin {
    
    var _call:CAPPluginCall!
    
    @objc func getCall() -> CAPPluginCall { return _call; };
    
    @objc func wxPayRequest(_ call: CAPPluginCall) {
        _call = call;
        
        print("进入wxPayRequest方法!")
        
        let req = PayReq()
        
        req.partnerId = call.getString("mch_id") ?? ""; // 商户号
        req.prepayId = call.getString("prepay_id") ?? ""; // 预支付交易会话标识
        req.nonceStr = call.getString("nonce") ?? ""; // 随机字符串
        req.timeStamp = UInt32(call.getString("timestamp") ?? "")! // 时间戳
        req.sign = call.getString("sign") ?? "" // 签名
        req.package = "Sign=WXPay"; // 签名
        
        call.save()
        
        if(!WXApi.isWXAppInstalled()){
            print("WXApi.isWXAppInstalled()")
            WechatCall("-8");
            return;
        }
        
        WXApi.send(req) { (bool) in
            if(!bool){
                WechatCall("-9");
            }
        }
    }
    
    @objc func aliPayRequest(_ call: CAPPluginCall) {
        _call = call;
        print("进入aliPayRequest方法!")
        
        let orderInfo = call.getString("orderInfo") ?? ""
        
        AlipaySDK.defaultService()?.payOrder(orderInfo, fromScheme: "alipay", callback: { (resultDic) in
            print(resultDic as Any)
            if let Alipayjson = resultDic as NSDictionary?{
                let resultStatus = Alipayjson.value(forKey: "resultStatus") as! String
                AlipayCall(resultStatus)
            }
        })
    }
}
