package com.jakting.opengapps.utils.device;

import android.os.Build;

public class GetDeviceInfo {

    public String getVendor() {
        return Build.MANUFACTURER;
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getCPU() {
        return Build.CPU_ABI;
    }

    public String getCPUshort() {
        String cpu = getCPU();
        if (cpu.contains("armeabi")) {
            cpu = "arm";
        } else if (cpu.contains("arm64")) {
            cpu = "arm64";
        } else if (cpu.contains("x86_64")) {
            cpu = "x86_64";
        } else if (cpu.equals("x86")) {
            cpu = "x86";
        }
        return cpu;
    }

    public String getOS(){
        String os = Build.VERSION.RELEASE;
        if (os.contains("9")){
            os = "9.0";
        }else if(os.contains("10")){
            os = "10.0";
        }
        return os;
    }
}
