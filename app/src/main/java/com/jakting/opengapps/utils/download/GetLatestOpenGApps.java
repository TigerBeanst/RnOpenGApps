package com.jakting.opengapps.utils.download;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jakting.opengapps.utils.device.GetDeviceInfo;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.stericson.RootTools.Constants.TAG;

public class GetLatestOpenGApps {

    public String date = "";
    public GetLatestOpenGApps(final Handler myHandler){
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://api.opengapps.org/list")
                .method("GET",null)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //请求失败执行的方法
            @Override
            public void onFailure(Call call, IOException e) {
            }
            //请求成功执行的方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String data = response.body().string().replaceAll("\\s*", "");
                GetDeviceInfo di = new GetDeviceInfo();
                JsonObject obj = new JsonParser().parse(data).getAsJsonObject();
                JsonObject archs = obj.get("archs").getAsJsonObject();
                JsonObject cpu_chip = archs.get(di.getCPUshort()).getAsJsonObject();
                date = cpu_chip.get("date").getAsString();
                Log.d(TAG, "获取日期: "+date);
                Message msg = Message.obtain();
                msg.what = 233;
                myHandler.sendMessage(msg);
            }
        });
    }



}
