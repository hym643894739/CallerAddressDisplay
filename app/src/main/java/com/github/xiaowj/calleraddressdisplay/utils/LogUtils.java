package com.github.xiaowj.calleraddressdisplay.utils;

import android.util.Log;

/**
 * Created by xiaowj on 2016/4/16 016.
 */
public class LogUtils {

    private static final String TAG = "xiaowj";

    public static void e(String s) {
        Log.e(TAG, s);
    }

    public static void e(int s) {
        Log.e(TAG, Integer.toString(s));
    }
}
