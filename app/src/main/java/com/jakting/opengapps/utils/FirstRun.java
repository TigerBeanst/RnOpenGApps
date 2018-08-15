package com.jakting.opengapps.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.jakting.opengapps.GuideActivity;

public class FirstRun {
    public static void FirstRun(Context context, View view,SharedPreferences data_of_download){
        /**
         * 首次运行
         *
         * @param context
         * @return
         */
        Log.d("debug","首次运行开始");
        SharedPreferences.Editor firstRun = context.getSharedPreferences("data", Context.MODE_PRIVATE).edit();
        SharedPreferences firstBool = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        if((firstBool.getBoolean("firstRun",true))){ //第一次运行
            Intent intent = new Intent(context, GuideActivity.class); //启动向导
            context.startActivity(intent);
            if(!(firstBool.contains("android_version"))){
                ((Activity)context).finish();
            }else{
                firstRun.putBoolean("firstRun",false);
                firstRun.apply();
            }
        }else{
            SharedPreferences av = context.getSharedPreferences("data",Context.MODE_PRIVATE);
            String avv = av.getString("android_version", "");
            if(avv.equals("")){
                Log.d("debug","获取系统版本时出问题");
            }else if(avv.equals("9.0")){
                GetLatest90.GetLatest90(context,view,data_of_download);
            }else{
                //获取最新下载地址
                GetLatest.GetLatest(context,view,data_of_download);
            }
        }
        Log.d("debug","首次运行结束");


    }
}
