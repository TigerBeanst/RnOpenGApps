package com.jakting.opengapps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        return view;
    }
    CardView do_card;
    SharedPreferences data_of_download;
    String a_v;
    String c_u;
    String v_r;
    SharedPreferences.Editor save_data;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.app_name);
        data_of_download = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        a_v = data_of_download.getString("android_version","");
        c_u = data_of_download.getString("cpu","");
        v_r = data_of_download.getString("var","");
        save_data = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        do_card = (CardView)getActivity().findViewById(R.id.state_latest);
        firstRun();
        myDeviceInfo();
        stateGApps();
        whichOne();
        do_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(progress==0)){
                    clickDownload();
                }
            }
        });
        setHasOptionsMenu(true);
        LinearLayout clickGApps = (LinearLayout)getActivity().findViewById(R.id.notice_click);
        clickGApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gappsurl =  Uri.parse("https://jakting.com/archives/gapps-links-what.html");
                startActivity(new Intent(Intent.ACTION_VIEW,gappsurl));
            }
        });

    }

    public static boolean isInstall(Context context, String packageName){
        /**
         * 判断是否安装 GApps，代码可适用于其他情况
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

    private void whichOne(){
        String inRel = a_v;
        String inCPU = c_u;
        getLatest();
    }

    private void clickDownload(){
        final String date = data_of_download.getString("date","");
        AlertDialog.Builder Aldialog = new AlertDialog.Builder(getActivity());
        Aldialog.setTitle(getString(R.string.download_confirm));
        Aldialog.setMessage(getString(R.string.download_confirm_1)+a_v+getString(R.string.download_confirm_2)+c_u+getString(R.string.download_confirm_3)+v_r);
        Aldialog.setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri download_url = Uri.parse("https://github.com/opengapps/"+c_u+"/releases/download/"+date+"/open_gapps-"+c_u+"-"+a_v+"-"+v_r+"-"+date+".zip");
                    startActivity(new Intent(Intent.ACTION_VIEW,download_url));
            }
        });
        Aldialog.setNeutralButton(R.string.correct, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent resetWizard = new Intent(getActivity(),GuideActivity.class);
                startActivity(resetWizard);
            }
        });
        AlertDialog al = Aldialog.create();
        al.show();

    }

    public void stateGApps(){
        boolean stateofGApps = isInstall(getActivity().getApplicationContext(),"com.google.android.gms");
        ImageView status_icon = (ImageView)getActivity().findViewById(R.id.status_icon);
        FrameLayout status_container = (FrameLayout)getActivity().findViewById(R.id.status_container);
        TextView status_text = (TextView)getActivity().findViewById(R.id.status_text);
        if(stateofGApps){
            status_icon.setImageResource(R.drawable.ic_check_circle);
            status_container.setBackgroundResource(R.color.darker_green);
            status_text.setText(R.string.gapps_ok);
            status_text.setTextColor(this.getResources().getColor(R.color.darker_green));
        }else{
            status_icon.setImageResource(R.drawable.ic_warning);
            status_container.setBackgroundResource(R.color.warning);
            status_text.setText(R.string.gapps_warning);
            status_text.setTextColor(this.getResources().getColor(R.color.warning));
        }
    }

    String osRelease = android.os.Build.VERSION.RELEASE; //Android 版本号
    String osRel = osRelease.substring(0,3);//截取前三位
    String osModel = Build.MODEL; //型号
    String osCPU = Build.CPU_ABI; //CPU 平台
    int osSDK = android.os.Build.VERSION.SDK_INT; // SDK 版本
    public void myDeviceInfo(){
        TextView myDeviceModelText = (TextView)getActivity().findViewById(R.id.my_device_model);
        TextView myDeviceOS = (TextView)getActivity().findViewById(R.id.my_device_sdk);
        TextView myDeviceCPU = (TextView)getActivity().findViewById(R.id.my_device_cpu);
        TextView status_latest = (TextView)getActivity().findViewById(R.id.status_latest);
        myDeviceModelText.setText(osModel);
        myDeviceCPU.setText(osCPU);
        myDeviceOS.setText("Android "+osRelease+" (API "+osSDK+")");
        status_latest.setText(R.string.loading);
    }


    public int progress = 0; //通用的存进度数值，虽然并没有什么卵用

    public String apiData;
    public void getLatest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String cpu_what = c_u;
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try{
                    URL url = new URL("https://api.github.com/repos/opengapps/"+cpu_what+"/releases/latest");
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) !=null){
                        response.append(line);
                    }
                    apiData = response.toString();
                    JSONObject json = new JSONObject(apiData);
                    String version = json.getString("tag_name");
                    save_data.putString("date",version).apply();
                    progress = 100;
                    TextView status_latest = (TextView)getActivity().findViewById(R.id.status_latest);
                    status_latest.setText(getString(R.string.latest_release)+" "+cpu_what+": "+version);
                    ProgressBar progressBar = (ProgressBar)getActivity().findViewById(R.id.progress_bar);
                    if(progress == 100){
                        progressBar.setVisibility(View.INVISIBLE);
                        ImageView status_github = (ImageView)getActivity().findViewById(R.id.status_github);
                        status_github.setImageResource(R.drawable.ic_download);
                        status_github.setVisibility(View.VISIBLE);
                        do_card.setClickable(true);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(reader!=null){
                        try{
                            reader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_reset:
                Intent resetWizard = new Intent(getActivity(),GuideActivity.class);
                startActivity(resetWizard);
                break;
            default:
                break;
        }
        return true;
    }

    private void firstRun(){
        SharedPreferences.Editor firstRun = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        SharedPreferences firstBool = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        if((firstBool.getBoolean("firstRun",true))){
            Intent intent = new Intent(getActivity(), GuideActivity.class);
            startActivity(intent);
            if(!(firstBool.contains("android_version"))){
                getActivity().finish();
            }else{
                firstRun.putBoolean("firstRun",false);
                firstRun.apply();
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

}