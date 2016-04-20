package com.github.xiaowj.calleraddressdisplay.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.github.xiaowj.calleraddressdisplay.R;
import com.github.xiaowj.calleraddressdisplay.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class GetPhoneLocService extends Service {

    public static final String NUM_EXTRA = "number";
    private final String GET_NUMBER_LOCAL = "http://apis.baidu.com/apistore/mobilenumber/mobilenumber";
    private WindowManager wManager;
    private ViewGroup phoneLoc;

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
        parameters.put("phone", number);
        ApiStoreSDK.execute(GET_NUMBER_LOCAL, ApiStoreSDK.GET, parameters, new ApiCallBack() {
            @Override
            public void onSuccess(int i, String s) {
                super.onSuccess(i, s);
                LogUtils.e(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int errNum = jsonObject.optInt("errNum");
                    if (0 == errNum) {
                        JSONObject data = jsonObject.optJSONObject("retData");
                        String supplier = data.getString("supplier");//运营商
                        String province = data.getString("province");
                        String city = data.getString("city");
                        showPhoneLocWindow(supplier, province, city);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showPhoneLocWindow(String supplier, String province, String city) {
        if (wManager != null) {
            if (phoneLoc != null) {
                wManager.removeView(phoneLoc);
            }
            wManager = null;
        }
        wManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity = Gravity.CENTER;
        phoneLoc = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.phone_local, null);
        TextView textView = (TextView) phoneLoc.findViewById(R.id.id_text_phone_local);
        TextView supplierText = (TextView) phoneLoc.findViewById(R.id.id_text_supplier);
        textView.setText(province + city);
        supplierText.setText(supplier);
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        phoneLoc.measure(w, h);
        int width = phoneLoc.getMeasuredWidth();
        int height = phoneLoc.getMeasuredHeight();
        params.width = width;
        params.height = height;
        wManager.addView(phoneLoc, params);
    }

}
