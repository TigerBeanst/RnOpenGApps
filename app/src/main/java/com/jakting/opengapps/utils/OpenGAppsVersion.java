package com.jakting.opengapps.utils;

import android.util.Log;
import com.stericson.RootTools.RootTools;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class OpenGAppsVersion {
    String g_prop = "/etc/g.prop";
    final File file = new File(g_prop);
    String getContext = "";

    public OpenGAppsVersion(){}
    public boolean testRoot() {
        if (RootTools.isAccessGiven()) {
            Log.d("debug","到了获取权限");
            //readGPROP();
            return true;
        }else {
            return false;
        }
    }

    public String readGPROP() {

        if (file.exists()) { //如果文件存在

            BufferedReader  buffered_reader=null;
            try
            {
                buffered_reader = new BufferedReader(new FileReader(g_prop));
                Log.d("debug","buffered_reader实例化，用FileReade读取了文件……？");
                String line;
                while ((line = buffered_reader.readLine()) != null)
                {
                    getContext += line;
                    //Log.d("debug",line);
                }
                getContext = getContext.substring(144,152);
                Log.d("debug",getContext);
            }
            catch (IOException e)
            {
                // TODO
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    if (buffered_reader != null)
                        buffered_reader.close();
                }
                catch (IOException ex)
                {
                    // TODO
                    ex.printStackTrace();
                }
            }

            return getContext;
        }else{
            //不存在
            getContext = "null";
            return getContext;
        }
    }
}
