package com.jakting.opengapps.utils;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakting.opengapps.R;

public class StateGApps {
    public static void StateGApps(Context context, View view) {
        /**
         * 根据当前设备 Google 套件的安装情况决定首页状态一栏的显示内容。
         *
         * @param context
         * @return
         */

        //判断是否安装 GMS
        boolean stateofGApps = IsInstall.IsInstall(context, "com.google.android.gms");

        ImageView status_icon = (ImageView) view.findViewById(R.id.status_icon);
        FrameLayout status_container = (FrameLayout) view.findViewById(R.id.status_container);
        TextView status_text = (TextView) view.findViewById(R.id.status_text);
        if (stateofGApps) {
            status_icon.setImageResource(R.drawable.ic_check_circle);
            status_container.setBackgroundResource(R.color.darker_green);
            status_text.setText(R.string.gapps_ok);
            status_text.setTextColor(view.getResources().getColor(R.color.darker_green));
        } else {
            status_icon.setImageResource(R.drawable.ic_warning);
            status_container.setBackgroundResource(R.color.warning);
            status_text.setText(R.string.gapps_warning);
            status_text.setTextColor(view.getResources().getColor(R.color.warning));
        }
    }
}
