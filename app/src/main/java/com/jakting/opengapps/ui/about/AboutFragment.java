package com.jakting.opengapps.ui.about;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.jakting.opengapps.R;

public class AboutFragment extends Fragment {

    private AboutViewModel aboutViewModel;
    View roott;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        aboutViewModel =
                ViewModelProviders.of(this).get(AboutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        roott = root;
        getMyApp();
        getVersion();
        return root;
    }

    private String[] getVersion(){
        String[] getV= {"",""};
        try{
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0);
            getV[0] = packageInfo.versionName;
            getV[1] = String.valueOf(packageInfo.versionCode);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return getV;
    }

    public void getMyApp(){
        String[] getV= getVersion();
        String version = getV[0]+"("+getV[1]+")";
        final TextInputEditText app_version = (TextInputEditText)roott.findViewById(R.id.input_app_version);
        app_version.setText(version);
        app_version.setKeyListener(null);
        final TextInputEditText app_dev = (TextInputEditText)roott.findViewById(R.id.input_app_dev);
        app_dev.setText("hjthjthjt");
        app_dev.setKeyListener(null);
    }


}
