package com.jakting.opengapps.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;

public class allinall {
    public static void allinall(Context context, View view) {
        /**
         * 声明变量，调用函数，执行一堆方法。
         *
         * @param context,view
         * @return
         */

        //Android 版本号
        String osRelease = android.os.Build.VERSION.RELEASE;
        String osRel = osRelease.substring(0, 3);//上面那个截取前三位

        //设备型号
        String osModel = Build.MODEL;

        //设备 CPU
        String osCPU = Build.CPU_ABI;

        //Android SDK 版本
        int osSDK = android.os.Build.VERSION.SDK_INT;

        //
        SharedPreferences data_of_download;
        SharedPreferences data_of_download_md5;
        data_of_download = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        data_of_download_md5 = context.getSharedPreferences("dataMD5", Context.MODE_PRIVATE);

        String a_v;
        String c_u;
        String v_r;

        /**
         * 正片开始
         */
        //根据当前设备信息显示内容
        myDeviceInfo.myDeviceInfo(view, osModel, osCPU, osRelease, osSDK);

        //根据当前设备 Google 套件的安装情况决定首页状态一栏的显示内容
        stateGApps.stateGApps(context, view);

        //性感下载，在线猛击
        clickDownload.clickDownload(context, data_of_download, data_of_download_md5);

    }
}
