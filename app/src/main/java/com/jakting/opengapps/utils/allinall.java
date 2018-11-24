package com.jakting.opengapps.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.jakting.opengapps.R;

public class AllInAll {
    public static void allinall(final Context context, final View view) {
        /**
         * 声明变量，调用函数，执行一堆方法。
         *
         * @param context,view
         * @return
         */

        //Android 版本号
        String osRelease = android.os.Build.VERSION.RELEASE;
        String osRel;
        if(osRelease.equals("9")){
            osRel = osRelease;
        }else{
            osRel = osRelease.substring(0, 3);//上面那个截取前三位
        }

        //设备型号
        String osModel = Build.MODEL;

        //设备 CPU
        String osCPU = Build.CPU_ABI;

        //Android SDK 版本
        int osSDK = android.os.Build.VERSION.SDK_INT;

        //
        final SharedPreferences data_of_download;
        data_of_download = context.getSharedPreferences("data", Context.MODE_PRIVATE);

/*        String a_v = data_of_download.getString("android_version","");
        String c_u = data_of_download.getString("cpu","");
        String v_r = data_of_download.getString("var","");*/


        CardView do_card = (CardView)view.findViewById(R.id.state_latest);
        /**
         * 正片开始
         */

        //首次运行
        FirstRun.FirstRun(context,view,data_of_download);

        //根据当前设备信息显示内容
        MyDeviceInfo.MyDeviceInfo(view, osModel, osCPU, osRelease, osSDK);

        //根据当前设备 Google 套件的安装情况决定首页状态一栏的显示内容
        StateGApps.StateGApps(context, view);

        SharedPreferences av = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        String avv = av.getString("android_version", "");
        if(avv.equals("")){
            Log.d("debug","获取系统版本时出问题");
        }else{
            //获取最新下载地址
            GetLatest.GetLatest(context,view,data_of_download);
        }





    }
}
