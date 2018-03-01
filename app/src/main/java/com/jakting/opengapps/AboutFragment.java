package com.jakting.opengapps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.psdev.licensesdialog.LicensesDialog;


public class AboutFragment extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_about, container, false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.title_about);
        findSoManyId();
        fillVersion();
    }

    private String[] getV={"",""};
    private String[] getVersion(){
        try{
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0);
            getV[0] = packageInfo.versionName;
            getV[1] = String.valueOf(packageInfo.versionCode);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return getV;
    }
    private void fillVersion(){
        getVersion();
        String versionN = getV[0];
        String versionC = getV[1];
        TextView app_version = (TextView)getActivity().findViewById(R.id.app_version);
        app_version.setText(versionN+"("+versionC+")");
    }

    private void findSoManyId(){
        LinearLayout developersView = (LinearLayout)getActivity().findViewById(R.id.developersView);
        developersView.setOnClickListener(this);
        LinearLayout licensesView = (LinearLayout)getActivity().findViewById(R.id.licensesView);
        licensesView.setOnClickListener(this);
        LinearLayout sourceCodeView = (LinearLayout)getActivity().findViewById(R.id.sourceCodeView);
        sourceCodeView.setOnClickListener(this);
        LinearLayout langView = (LinearLayout)getActivity().findViewById(R.id.langView);
        langView.setOnClickListener(this);
    }

    public void onMultipleClick(final View view) {
        new LicensesDialog.Builder(getActivity())
                .setNotices(R.raw.notices)
                .build()
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.developersView:
                Uri blogurl = Uri.parse("https://jakting.com");
                startActivity(new Intent(Intent.ACTION_VIEW,blogurl));
                break;
            case R.id.licensesView:
                onMultipleClick(v);
                break;
            case R.id.sourceCodeView:
                Uri source_url = Uri.parse("https://github.com/hjthjthjt/RnOpenGApps");
                startActivity(new Intent(Intent.ACTION_VIEW,source_url));
                break;
            case R.id.langView:
                AlertDialog.Builder tr_lang = new AlertDialog.Builder(getActivity());
                tr_lang.setTitle(getString(R.string.about_tran_label));
                tr_lang.setMessage(R.string.about_tran_mess);
                tr_lang.setPositiveButton(R.string.about_tran_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri values_url = Uri.parse("https://github.com/hjthjthjt/RnOpenGApps/blob/master/app/src/main/res/values/strings.xml");
                        startActivity(new Intent(Intent.ACTION_VIEW,values_url));
                    }
                });
                AlertDialog langDia = tr_lang.create();
                langDia.show();
                break;
            default:
                break;
        }
    }

}
