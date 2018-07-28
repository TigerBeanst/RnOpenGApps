package com.jakting.opengapps.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.jakting.opengapps.GuideActivity;
import com.jakting.opengapps.R;


public class clickDownload {
    public static void clickDownload(Context context,SharedPreferences data_of_download, SharedPreferences data_of_download_md5){
        /**
         * 性感下载，在线猛击。
         *
         * @param context,view
         * @return
         */

        final String date = data_of_download.getString("date","");
        AlertDialog.Builder Aldialog = new AlertDialog.Builder(context);
        Aldialog.setTitle(context.getString(R.string.download_confirm));
        Aldialog.setMessage(context.getString(R.string.download_confirm_1)+a_v+context.getString(R.string.download_confirm_2)+c_u+context.getString(R.string.download_confirm_3)+v_r+context.getString(R.string.download_confirm_4));
        Aldialog.setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri download_url = Uri.parse("https://github.com/opengapps/"+c_u+"/releases/download/"+date+"/open_gapps-"+c_u+"-"+a_v+"-"+v_r+"-"+date+".zip");
                context.startActivity(new Intent(Intent.ACTION_VIEW,download_url));
            }
        });
        Aldialog.setNegativeButton(R.string.checkMD5, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showMD5();
            }
        });
        Aldialog.setNeutralButton(R.string.correct, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent resetWizard = new Intent(context,GuideActivity.class);
                startActivity(resetWizard);
            }
        });
        AlertDialog al = Aldialog.create();
        al.show();
    }
}
