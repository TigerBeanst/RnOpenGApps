package com.jakting.opengapps.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakting.opengapps.R;

import java.net.URL;

public class StateGApps {

    static OpenGAppsVersion openGAppsVersion;
    static String getContext;
    static boolean isRoot;
    static boolean isRootSP;

    public static void StateGApps(final Context context, View view) {
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
        CardView state_card = (CardView) view.findViewById(R.id.state_card);
        TextView status_text = (TextView) view.findViewById(R.id.status_text);
        if (stateofGApps) {
            status_icon.setImageResource(R.drawable.ic_check_circle);
            status_container.setBackgroundResource(R.color.darker_green);
            state_card.setClickable(true);
            status_text.setText(R.string.gapps_ok);
            status_text.setTextColor(view.getResources().getColor(R.color.darker_green));
        } else {
            status_icon.setImageResource(R.drawable.ic_warning);
            status_container.setBackgroundResource(R.color.warning);
            state_card.setClickable(false);
            status_text.setText(R.string.gapps_warning);
            status_text.setTextColor(view.getResources().getColor(R.color.warning));
        }

        state_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGAppsVersion = new OpenGAppsVersion();
                //final  = openGAppsVersion.readGPROP();
                SharedPreferences rootOr = context.getSharedPreferences("data", Context.MODE_PRIVATE);
                isRootSP = rootOr.getBoolean("root", false);
                Log.d("debug","这里isRootSP的值是："+isRootSP);
                if (isRootSP) {
                    afterGrantRoot(context);
                } else {
                    //Toast.makeText(context,"→"+getContext+"←",Toast.LENGTH_LONG).show();
                    //看看是否授予了 Root 权限
                    final AlertDialog.Builder root_grant = new AlertDialog.Builder(context);
                    root_grant.setTitle(context.getString(R.string.gapps_root));
                    root_grant.setMessage(context.getString(R.string.gapps_root_msg));
                    root_grant.setPositiveButton(context.getString(R.string.gapps_root_msg_get), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //返回是否 Root
                            isRoot = openGAppsVersion.testRoot();
                            SharedPreferences.Editor rootEd= context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
                            if (isRoot) {
                                rootEd.putBoolean("root",true);
                                Log.d("debug","这里把值设置为true");
                                rootEd.apply();
                                Toast.makeText(context, context.getString(R.string.gapps_root_msg_gett), Toast.LENGTH_LONG).show();
                                afterGrantRoot(context);
                            } else {
                                rootEd.putBoolean("root",false);
                                Log.d("debug","这里把值设置为false");
                                rootEd.apply();
                                Toast.makeText(context, context.getString(R.string.gapps_root_msg_get_no), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    root_grant.setNegativeButton(context.getString(R.string.gapps_root_msg_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(context, context.getString(R.string.gapps_root_msg_noo), Toast.LENGTH_LONG).show();
                        }
                    });
                    root_grant.setNeutralButton(context.getString(R.string.gapps_root_msg_why), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String url = "https://jakting.com/archives/why-rnopengapps-need-root-permission.html";
                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                            builder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
                            CustomTabsIntent customTabsIntent = builder.build();
                            customTabsIntent.launchUrl(context, Uri.parse(url));
                        }
                    });
                    AlertDialog rg = root_grant.create();
                    rg.show();
                }
            }
        });
    }

    public static void afterGrantRoot(final Context context) {

        getContext = openGAppsVersion.readGPROP();
        //授予 Root 权限后
        AlertDialog.Builder watch_status = new AlertDialog.Builder(context);
        watch_status.setTitle(context.getString(R.string.gapps_details_title));
        if (getContext.equals("null")) {
            watch_status.setMessage(context.getString(R.string.gapps_details_error));
        } else {
            watch_status.setMessage(context.getString(R.string.gapps_details_msg) + getContext);
        }
        AlertDialog al = watch_status.create();
        al.show();
    }
}
