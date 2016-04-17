package com.github.xiaowj.calleraddressdisplay.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.xiaowj.calleraddressdisplay.R;
import com.github.xiaowj.calleraddressdisplay.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetPhoneLocIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.github.xiaowj.calleraddressdisplay.services.action.FOO";
    private static final String ACTION_BAZ = "com.github.xiaowj.calleraddressdisplay.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.github.xiaowj.calleraddressdisplay.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.github.xiaowj.calleraddressdisplay.services.extra.PARAM2";

    private final String GET_NUMBER_LOCAL = "http://apis.baidu.com/apistore/mobilenumber/mobilenumber?phone=";
    private final String API_KEY = "ab10be75d1fa2af81d75e6d7fab70c3b";
    private WindowManager wManager;
    private View phoneLoc;
    private Context context;

    public GetPhoneLocIntentService() {
        super("GetPhoneLocIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, GetPhoneLocIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, GetPhoneLocIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
//        throw new UnsupportedOperationException("Not yet implemented");
        try {
            URL url = new URL(GET_NUMBER_LOCAL + param1);
            LogUtils.e(GET_NUMBER_LOCAL + param1);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey", API_KEY);
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            StringBuffer sbf = new StringBuffer();
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
            }
            reader.close();
            String result = sbf.toString();
            LogUtils.e(result);
            JSONObject jsonObject = new JSONObject(result);
            int errNum = jsonObject.optInt("errNum");
            if (0 == errNum) {
                JSONObject data = jsonObject.optJSONObject("retData");
                String supplier = data.getString("supplier");//运营商
                String province = data.getString("province");
                String city = data.getString("city");
                showPhoneLocWindow(supplier, province, city);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("出现错误");
        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 在窗口上显示号码的归属地
     *
     * @param supplier 运营商
     * @param province 省份
     * @param city     城市
     */
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
        params.format = 1;
        params.flags = 40;
        params.gravity = Gravity.CENTER;
        phoneLoc = LayoutInflater.from(this).inflate(R.layout.phone_local, null);
        TextView textView = (TextView) phoneLoc.findViewById(R.id.id_text_phone_local);
        textView.setText(city);
        int width = 500;
        int height = 200;
        params.width = width;
        params.height = height;
        wManager.addView(phoneLoc, params);
    }
}
