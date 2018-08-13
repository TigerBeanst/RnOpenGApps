package com.jakting.opengapps.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakting.opengapps.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Handler;

public class GetLatest {
    static Handler handler;
    public static int GetLatest(final Context context, final View view, final SharedPreferences data_of_download) {
        /**
         * 获取最新下载地址
         *
         * @param context,view,data_of_download
         * @return
         */
        Log.d("debug","开始执行 GetLatest");
        final String c_u = data_of_download.getString("cpu", "");
        final String cpu_what = c_u;
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                CardView do_card = (CardView) view.findViewById(R.id.state_latest);
                TextView status_latest = (TextView) view.findViewById(R.id.status_latest);
                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
                ImageView status_github = (ImageView) view.findViewById(R.id.status_github);
                SharedPreferences getV = context.getSharedPreferences("data",Context.MODE_PRIVATE);
                String getVersion = getV.getString("date","");
                status_latest.setText(context.getString(R.string.latest_release) + " " + cpu_what + ": " + getVersion);
                Log.d("debug", "获取后改文本改图形");
                progressBar.setVisibility(View.INVISIBLE);
                status_github.setImageResource(R.drawable.ic_download);
                status_github.setVisibility(View.VISIBLE);
                do_card.setClickable(true);

                //性感下载，在线猛击
                do_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClickDownload.ClickDownload(context, data_of_download);
                    }
                });
                Log.d("debug", "卡片可以点了");
                Log.d("debug", "完成获取最新地址");
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                String apiData;
                SharedPreferences.Editor save_data;
                save_data = context.getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    Log.d("debug", "用URL连接");
                    URL url = new URL("https://api.github.com/repos/opengapps/" + cpu_what + "/releases/latest");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    Log.d("debug", "有回应辣");
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    apiData = response.toString();
                    Log.d("debug", "转化为字符串");
                    JSONObject json = new JSONObject(apiData);
                    String version = json.getString("tag_name");
                    Log.d("debug", "获取tagname:" + version);
                    save_data.putString("date", version).apply();

                    Log.d("debug", "理论获取好了，下面是进度100，此时date内容是"+version+" end");

                    handler.sendEmptyMessage(0);



                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
        return 100;
    }
}
