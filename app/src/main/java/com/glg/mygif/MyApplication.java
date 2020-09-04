package com.glg.mygif;

import android.app.Application;
import android.content.Context;

import com.bugtags.library.Bugtags;
import com.ekwing.http.common.Config;
import com.ekwing.http.common.HttpProxy;
import com.ekwing.http.okgoclient.OkGoWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @package: com.glg.mygif
 * @className: MyApplication
 * @author: gao
 * @date: 2020/8/5 18:39
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initOkHttp();
        Bugtags.start("0ab41325ef6494edd43a9c2a2fdacd0f", this, Bugtags.BTGInvocationEventBubble);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
    private void initOkHttp() {
        //引入新的Ok框架
        Map head = new HashMap<String,String>();
        head.put("v","EF7F71BEF31F5750A69345CDA28841AB");
        head.put("User-Agent","GifFun Android");
        head.put("appv","1.0.3");
        head.put("apps","B54467636EAAFE160CF3718F9A30ADF9");
        Config config = new Config.Builder()
                .connectTimeout(Config.DEFAULT_TIMEOUT, TimeUnit.SECONDS)//超时时间
                .readTimeout(Config.DEFAULT_TIMEOUT, TimeUnit.SECONDS)//超时时间
                .writeTimeout(Config.DEFAULT_TIMEOUT, TimeUnit.SECONDS)//超时时间
                .setEnableDns(true)//启用自定DNS
                .setLogEnable(BuildConfig.DEBUG)//开始日志
                .setLogTag("okHttp")
                .setHeaders(head)
                .setRetryCount(Config.MAX_RETRY)//重试次数
                .build();
        HttpProxy.getInstance().initClient(new OkGoWrapper(this, config));
    }
}
