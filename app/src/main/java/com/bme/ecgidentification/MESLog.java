package com.bme.ecgidentification;

/**
 * Created by sht on 2017/6/22.
 */

import android.util.Log;

import java.util.List;

/**
 * message log
 */

public class MESLog {

    private static boolean enableLog = true;
    /**
     * 打开log观察调试信息
     */
    public static void enableLog() {
        enableLog = true;
    }
    /**
     * 打开log观察调试信息
     */
    public static void disableLog() {
        enableLog = false;
    }
    /**
     * @return 是否打开了log
     */
    public static boolean isEnableLog() {
        return enableLog;
    }
    /**
     * info级别log
     *
     * @param msg
     */
    public static void logI(String TAG,String msg) {
        if (enableLog) {
            Log.i(TAG, msg);
        }
    }
    /**
     * verbose级别log
     *
     * @param msg
     */
    public static void logV(String TAG,String msg) {
        if (enableLog) {
            Log.v(TAG, msg);
        }
    }
    /**
     * warning级别log
     *
     * @param msg
     */
    public static void logW(String TAG,String msg) {
        if (enableLog) {
            Log.w(TAG, msg);
        }
    }
    /**
     * debug级别log
     *
     * @param msg
     */
    public static void logD(String TAG,String msg) {
        if (enableLog) {
            Log.d(TAG, msg);
        }
    }
    /**
     * error级别log
     *
     * @param msg
     */
    public static void logE(String TAG,String msg) {
        if (enableLog) {
            Log.e(TAG, msg);
        }
    }

    /**
     * error级别log
     *
     * @param msg
     */
    public static void logE(String TAG,String[] msg) {
        if (enableLog) {
            for (int i= 0;i<msg.length;i++){
                Log.e(TAG,msg[i]);
            }
        }
    }

    /**
     * error级别log
     *
     * @param msg
     */
    public static void logE(String TAG,List<String> msg) {
        if (enableLog) {
            for (int i= 0;i<msg.size();i++){
                Log.e(TAG,msg.get(i));
            }
        }
    }
}
