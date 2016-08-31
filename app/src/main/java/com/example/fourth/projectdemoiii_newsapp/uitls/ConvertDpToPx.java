package com.example.fourth.projectdemoiii_newsapp.uitls;

import android.app.Activity;
import android.hardware.display.DisplayManager;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2016/8/31.
 */
public class ConvertDpToPx {

    //将 dp单位 转为 px像素单位
    public static int convertDpToPx(int dp, Activity activity){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float density = displayMetrics.density;//

        float px = dp * density;
        return (int) px;


    }
}
