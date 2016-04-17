package com.github.xiaowj.calleraddressdisplay.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.github.xiaowj.calleraddressdisplay.utils.LogUtils;

public class GetPhoneLocService extends Service {

    public static final String NUM_EXTRA = "number";
    private final String GET_NUMBER_LOCAL = "http://apis.baidu.com/apistore/mobilephoneservice/mobilephone";

    public GetPhoneLocService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static void startActionFoo(Context context, String number) {
        Intent intent = new Intent(context, GetPhoneLocService.class);
        intent.putExtra(NUM_EXTRA, number);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String number = intent.getStringExtra(NUM_EXTRA);

        Parameters parameters = new Parameters();
        parameters.put("tel",number);
        ApiStoreSDK.execute(GET_NUMBER_LOCAL,ApiStoreSDK.GET,parameters,new ApiCallBack(){
            @Override
            public void onSuccess(int i, String s) {
                super.onSuccess(i, s);
                LogUtils.e(s);
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
