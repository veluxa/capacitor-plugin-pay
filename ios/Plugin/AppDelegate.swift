//
//  AppDelegate.swift
//  Plugin
//
//  Created by Max on 2020/8/30.
//  Copyright © 2020 Max Lynch. All rights reserved.
//

import UIKit
import Capacitor

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate, WXApiDelegate {
    
    var window: UIWindow?
    
    internal func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        let appId = CAPConfig().getString("wxappId") ?? ""
        let universalLink = CAPConfig().getString("universalLink") ?? ""
        print("appId:",appId)
        print("universalLink",universalLink)
        WXApi.registerApp(appId, universalLink: universalLink);
        return true
    }
    
    func application(_ application: UIApplication, handleOpen url: URL) -> Bool {
        return WXApi.handleOpen(url, delegate: self)
    }
    
    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        
        if url.host == "safepay"{
            AlipaySDK.defaultService().processOrder(withPaymentResult: url){
                value in
                let code = value!
                let resultStatus = code["resultStatus"] as!String
                AlipayCall(resultStatus)
            }
        }else {
            return WXApi.handleOpen(url, delegate: self)
        }
        return true
    }
    
    func onReq(_ req: BaseReq) {
    }
    
    func onResp(_ resp: BaseResp) {
        let code = WXErrCode(resp.errCode);
        WechatCall(code);
    }
}
