package com.jakting.opengapps.utils;

import android.view.View;
import android.widget.TextView;

import com.jakting.opengapps.R;

public class myDeviceInfo {
    public static void myDeviceInfo(View view, String osModel, String osCPU, String osRelease, int osSDK) {
        /**
         * 根据当前设备信息显示内容。
         *
         * @param context
         * @return
         */

        TextView myDeviceModelText = (TextView) view.findViewById(R.id.my_device_model);
        TextView myDeviceOS = (TextView) view.findViewById(R.id.my_device_sdk);
        TextView myDeviceCPU = (TextView) view.findViewById(R.id.my_device_cpu);
        TextView status_latest = (TextView) view.findViewById(R.id.status_latest);
        myDeviceModelText.setText(osModel);
        myDeviceCPU.setText(osCPU);
        myDeviceOS.setText("Android " + osRelease + " (API " + osSDK + ")");
        status_latest.setText(R.string.loading);
    }
}
