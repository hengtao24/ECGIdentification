package com.bme.ecgidentification;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by sht on 2017/2/16.
 */

public class DisplayUtil {

    private static int screenHeight = -1;
    private static int screenWidth = -1;

    /*
     * transfrom the dip to px according to phone pixels
     */
    public static int dip2px(Context context, float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getScreenHeight(Context context){
        if (screenHeight != -1){
            return screenHeight;
        }
        WindowManager mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        return screenHeight;
    }

    public static int getScreenWidth(Context context){
        if (screenWidth != -1){
            return screenWidth;
        }
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        return screenWidth;
    }
}
