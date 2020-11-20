# capacitor-plugin-pay
[ios/android] wechat pay &amp; alipay

[demo](https://github.com/veluxa/capacitor-plugin-pay-demo) (如需正常支付需修改支付参数，参考下方)

[android]
修改 android/build.gradle 文件中 wxPayPackageName 的值为你app在微信支付上登记的appid，ios 无需此操作

> **配置**：

修改项目目录下 `capacitor.config.json` :
```json
{
  "appId": "你的应用appid",
  "wxappId": "你的应用微信支付appid",
  "universalLink": "你的universalLink",
  "appName": "你的app名字",
  "bundledWebRuntime": false,
  "npmClient": "npm",
  "webDir": "www",
  "plugins": {
    "SplashScreen": {
      "launchShowDuration": 0
    }
  },
  "cordova": {}
}
```


Android配置方法：

在根目录下找到 android 目录 ，依次找到 android/src/main/java/**** (嵌套包名文件夹)/activities/MainActivity.java，修改：

```ts
......
import com.veluxa.plugins.FileDownloader;

public class MainActivity extends BridgeActivity {
    ......
    this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
        ......
        add(FileDownloader.class);
    }});
    ......
}
```

IOS配置方法：
无需配置

> **调用**：

在需要使用到插件的文件，添加：

```ts
......
import 'capacitor-plugin-pay';
import { Plugins } from '@capacitor/core';
const { Pay } = Plugins;

......

// 微信支付
const wxPayParams = {
    mch_id: xxxx,
    prepay_id: xxxxx,
    nonce: xxxx,
    timestamp: xxxxx,
    sign: xxxx
};
Pay.wxPayRequest(wxPayParams).then(res => console.log('wx成功：',res))
.catch(err => { console.log('wx失败：',err) });

// 支付宝支付

Pay.aliPayRequest({ orderInfo: payUrlStr }).then(res => console.log('zfb成功：', res))
    .catch(err => { console.log('zfb失败：', err) });
......
```