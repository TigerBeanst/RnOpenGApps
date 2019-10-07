package com.jakting.opengapps.ui.download;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jakting.opengapps.R;
import com.jakting.opengapps.utils.download.DownUtils;
import com.jakting.opengapps.utils.download.GetLatestOpenGApps;
import com.liulishuo.filedownloader.FileDownloader;

import static com.stericson.RootTools.Constants.TAG;

public class DownloadFragment extends Fragment implements View.OnClickListener{

    private DownloadViewModel downloadViewModel;
    private String date = "";
    GetLatestOpenGApps getLatestOpenGApps;
    View roott;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_download_download:
                Log.d(TAG, "onClick: startDownload");
                clickDown();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        downloadViewModel =
                ViewModelProviders.of(this).get(DownloadViewModel.class);
        View root = inflater.inflate(R.layout.fragment_download, container, false);
        roott = root;
        SharedPreferences sp = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        getLatestOpenGApps = new GetLatestOpenGApps(myHandler);
        MaterialButton downloadButton = root.findViewById(R.id.button_download_download);
        downloadButton.setOnClickListener(this);
        return root;
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 233:
                    date = getLatestOpenGApps.date;
                    Log.d(TAG, "handleMessage: "+date);
                    setDate(roott);
                    break;
            }
            super.handleMessage(msg);
        }
    };
//https://downloads.sourceforge.net/project/opengapps/arm64/20190926/open_gapps-arm64-9.0-tvstock-20190926.zip?r=&ts=1569487433&use_mirror=autoselect
    public void setDate(View root){
        final TextView text_opengapps_version_get = root.findViewById(R.id.text_opengapps_version_get);
        final ProgressBar progress_circular = root.findViewById(R.id.progress_circular);
        final ImageView img_update = root.findViewById(R.id.img_update);
        final MaterialButton button_download_download = root.findViewById(R.id.button_download_download);
        text_opengapps_version_get.setText(date);
        progress_circular.setVisibility(View.INVISIBLE);
        img_update.setVisibility(View.VISIBLE);
        button_download_download.setEnabled(true);
        button_download_download.setStrokeColorResource(R.color.colorPrimary);


    }

    public void clickDown(){
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle(R.string.text_dialog_title_choose)
                .setPositiveButton(R.string.text_dialog_option_1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        download();
                    }
                })
                .setNegativeButton(R.string.text_dialog_option_2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNeutralButton(R.string.text_dialog_option_3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();





    }

    public void download(){
        FileDownloader.setup(getActivity());
        //https://downloads.sourceforge.net/project/opengapps/arm64/20191001/open_gapps-arm64-9.0-micro-20191001.zip?r=&ts=1569937679&use_mirror=autoselect
        DownUtils downUtils = new DownUtils("https://qd.myapp.com/myapp/qqteam/pcqq/PCQQ2019.exe","open_gapps-arm64-9.0-tvstock-20190926.zip",getActivity());
        downUtils.downloadOpenGApps(roott);
    }
}