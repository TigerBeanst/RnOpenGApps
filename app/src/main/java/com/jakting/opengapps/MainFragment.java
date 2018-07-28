package com.jakting.opengapps;

import com.jakting.opengapps.utils.allinall;
import com.jakting.opengapps.utils.isInstall;
import com.jakting.opengapps.utils.stateGApps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
    SharedPreferences data_of_download_md5;

    SharedPreferences.Editor save_data;
    SharedPreferences.Editor md5data;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.app_name);
        data_of_download = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        data_of_download_md5 = getActivity().getSharedPreferences("dataMD5",Context.MODE_PRIVATE);
        a_v = data_of_download.getString("android_version","");
        c_u = data_of_download.getString("cpu","");
        v_r = data_of_download.getString("var","");
        save_data = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        md5data = getActivity().getSharedPreferences("dataMD5",Context.MODE_PRIVATE).edit();
        do_card = (CardView)getActivity().findViewById(R.id.state_latest);
        firstRun();
        myDeviceInfo();

        Context context = this.getActivity();
        View view = this.getView();
        //程序启动时执行所需方法
        allinall.allinall(context,view);


        stateGApps.stateGApps(view);
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

    private void whichOne(){
        String inRel = a_v;
        String inCPU = c_u;
        getLatest();
    }

    public String apiDataMD5;

/*private void md5Get(){
        final String date = data_of_download.getString("date","");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String cpu_what = c_u;
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try{
                    URL url = new URL("https://github.com/opengapps/"+c_u+"/releases/download/"+date+"/open_gapps-"+c_u+"-"+a_v+"-"+v_r+"-"+date+".zip.md5");
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
                    apiDataMD5 = response.toString();
                    apiDataMD5 = apiDataMD5.substring(0,32);
                    SharedPreferences.Editor md5data = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
                    md5data.putString("md5data",apiDataMD5).apply();
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
    }*/

    private void showMD5(){
        final String date = data_of_download.getString("date","");
        final String md5data = data_of_download_md5.getString("md5data","");
        AlertDialog.Builder md5Dialog = new AlertDialog.Builder(getActivity());
        md5Dialog.setTitle(getString(R.string.checkMD5_title)+" "+date);
        md5Dialog.setMessage("MD5:\n"+ md5data);
        AlertDialog md5dia = md5Dialog.create();
        md5dia.show();

    }

    private void clickDownload(){
        final String date = data_of_download.getString("date","");
        AlertDialog.Builder Aldialog = new AlertDialog.Builder(getActivity());
        Aldialog.setTitle(getString(R.string.download_confirm));
        Aldialog.setMessage(getString(R.string.download_confirm_1)+a_v+getString(R.string.download_confirm_2)+c_u+getString(R.string.download_confirm_3)+v_r+getString(R.string.download_confirm_4));
        Aldialog.setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri download_url = Uri.parse("https://github.com/opengapps/"+c_u+"/releases/download/"+date+"/open_gapps-"+c_u+"-"+a_v+"-"+v_r+"-"+date+".zip");
                    startActivity(new Intent(Intent.ACTION_VIEW,download_url));
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
                Intent resetWizard = new Intent(getActivity(),GuideActivity.class);
                startActivity(resetWizard);
            }
        });
        AlertDialog al = Aldialog.create();
        al.show();

    }



    String osRelease = android.os.Build.VERSION.RELEASE; //Android 版本号
    String osRel = osRelease.substring(0,3);//截取前三位
    String osModel = Build.MODEL; //型号
    String osCPU = Build.CPU_ABI; //CPU 平台
    int osSDK = android.os.Build.VERSION.SDK_INT; // SDK 版本

    public void myDeviceInfo() {
        TextView myDeviceModelText = (TextView) getActivity().findViewById(R.id.my_device_model);
        TextView myDeviceOS = (TextView) getActivity().findViewById(R.id.my_device_sdk);
        TextView myDeviceCPU = (TextView) getActivity().findViewById(R.id.my_device_cpu);
        TextView status_latest = (TextView) getActivity().findViewById(R.id.status_latest);
        myDeviceModelText.setText(osModel);
        myDeviceCPU.setText(osCPU);
        myDeviceOS.setText("Android " + osRelease + " (API " + osSDK + ")");
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
                    ProgressBar progressBar = (ProgressBar)getActivity().findViewById(R.id.progress_bar);
                    ImageView status_github = (ImageView)getActivity().findViewById(R.id.status_github);
                    URL url5 = new URL("https://github.com/opengapps/"+c_u+"/releases/download/"+version+"/open_gapps-"+c_u+"-"+a_v+"-"+v_r+"-"+version+".zip.md5");
                    connection = (HttpURLConnection)url5.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in5 = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in5));
                    StringBuilder response5 = new StringBuilder();
                    String line5;
                    while((line5 = reader.readLine()) !=null){
                        response5.append(line5);
                    }
                    apiDataMD5 = response5.toString();
                    apiDataMD5 = apiDataMD5.substring(0,32);
                    md5data.putString("md5data",apiDataMD5).apply();
                    if(progress == 100){
                        status_latest.setText(getString(R.string.latest_release)+" "+cpu_what+": "+version);
                        progressBar.setVisibility(View.INVISIBLE);
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