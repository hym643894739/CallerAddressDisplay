package com.github.xiaowj.calleraddressdisplay.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by xiaowj on 2016/4/16 016.
 *
 * 用来判断是否有网
 */
public class NetWorkUtils {
    public static boolean getNetWorkIsConn(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null){
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            for (int i = 0; i < infos.length; i++) {
                if (infos[i].isConnected()){
                    return true;
                }
            }
        }
        return false;
    }
}
