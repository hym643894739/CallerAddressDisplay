package com.github.xiaowj.calleraddressdisplay;

import android.app.Application;

import com.baidu.apistore.sdk.ApiStoreSDK;

/**
 * Created by wuyif on 2016/4/16 016.
 */
public class PhoneLocApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApiStoreSDK.init(this,"ab10be75d1fa2af81d75e6d7fab70c3b");
    }
}
