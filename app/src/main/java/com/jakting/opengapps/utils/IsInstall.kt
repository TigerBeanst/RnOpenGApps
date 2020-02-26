package com.jakting.opengapps.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

class IsInstall{
    companion object{
        fun IsInstall(context: Context, packageName:String):Boolean{
            try {
                var pkgInfo: PackageInfo = context.packageManager.getPackageInfo(packageName.trim(),
                    PackageManager.GET_ACTIVITIES)
                if (packageName!=null){
                    return true
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                return false
            }
            return false
        }
    }
}