package com.jakting.opengapps.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class IsInstall {
    public static boolean IsInstall(Context context,String packageName){
        /**
         * 判断是否安装某应用，借助包名判断，代码可适用于其他情况
         *
         * @param context,packageName
         * @return 安装返回 True，否则返回 False
         *
         */
        try {
            PackageInfo pkginfo = context.getPackageManager().getPackageInfo(packageName.trim(), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            if(packageName!=null){
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
