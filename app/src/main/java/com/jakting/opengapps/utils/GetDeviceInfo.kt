package com.jakting.opengapps.utils

import android.os.Build

class GetDeviceInfo {

    fun getVendor(): String {
        return Build.MANUFACTURER
    }

    fun getModel(): String {
        return Build.MODEL
    }

    fun getCPU(): String {
        return Build.SUPPORTED_ABIS[0]
    }

    fun getCPUshort():String{
        var cpu = getCPU()
        if (cpu.contains("armeabi")) {
            cpu = "arm"
        } else if (cpu.contains("arm64")) {
            cpu = "arm64"
        } else if (cpu.contains("x86_64")) {
            cpu = "x86_64"
        } else if (cpu.equals("x86")) {
            cpu = "x86"
        }
        return cpu
    }

    fun getOS():String{
        var os = Build.VERSION.RELEASE
        if (os.contains("9")){
            os = "9.0"
        }else if(os.contains("10")){
            os = "10.0"
        }
        return os
    }
}