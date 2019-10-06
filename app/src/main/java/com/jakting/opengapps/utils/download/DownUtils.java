package com.jakting.opengapps.utils.download;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jakting.opengapps.R;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
import com.liulishuo.filedownloader.FileDownloader;

import static com.stericson.RootTools.Constants.TAG;

public class DownUtils {

    private String download_url = "";
    private String file_name = "";
    private Context context;

    public DownUtils(String download_url, String file_name, Context context){
        this.download_url = download_url;
        this.context = context;
        this.file_name = context.getExternalFilesDir("opengapps").getAbsolutePath()+"/"+file_name;
        Log.d(TAG, "DownUtils: 下载路径"+this.file_name);
    }

    public void downloadOpenGApps(final View root){
        FileDownloader.getImpl().create(download_url).setPath(file_name).setListener(new FileDownloadLargeFileListener() {
            @Override
            protected void pending(BaseDownloadTask task, long soFarBytes, long totalBytes) {

            }

            @Override
            protected void progress(BaseDownloadTask task, long soFarBytes, long totalBytes) {
                TextView tx_progress = root.findViewById(R.id.text_progress);
                int percent=(int) ((double) soFarBytes / (double) totalBytes * 100);
                tx_progress.setText("("+percent+"%"+")");
            }

            @Override
            protected void paused(BaseDownloadTask task, long soFarBytes, long totalBytes) {

            }

            @Override
            protected void completed(BaseDownloadTask task) {
                Log.d(TAG, "completed: 下载完毕");

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {

            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        }).start();
    }
}
