package com.github.xiaowj.calleraddressdisplay.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.github.xiaowj.calleraddressdisplay.services.GetPhoneLocIntentService;
import com.github.xiaowj.calleraddressdisplay.services.GetPhoneLocService;
import com.github.xiaowj.calleraddressdisplay.utils.LogUtils;
import com.github.xiaowj.calleraddressdisplay.utils.NetWorkUtils;

/**
 * Created by xiaowj on 2016/4/16 016.
 */
public class PhoneReceiver extends BroadcastReceiver {

    private Context context;
    private TelephonyManager tm;
    private static boolean isGetingPhoneLoc = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if (!NetWorkUtils.getNetWorkIsConn(context)) {
            //没有网~~
            return;
        }

        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    //电话挂断
                    LogUtils.e("CALL_STATE_IDLE");
                    isGetingPhoneLoc = false;
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //电话铃响
                    LogUtils.e("CALL_STATE_RINGING");
                    if (!isGetingPhoneLoc) {
                        GetPhoneLocIntentService.startActionFoo(context,incomingNumber,"");
                        isGetingPhoneLoc = true;
                    }
                    break;
            }
        }

    };


}
