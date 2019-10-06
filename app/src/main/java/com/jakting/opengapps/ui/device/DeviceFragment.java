package com.jakting.opengapps.ui.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.jakting.opengapps.R;
import com.jakting.opengapps.utils.device.GetDeviceInfo;
import com.jakting.opengapps.utils.device.IsInstall;

public class DeviceFragment extends Fragment {

    private DeviceViewModel deviceViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        deviceViewModel =
                ViewModelProviders.of(this).get(DeviceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_device, container, false);
        getMyDevice(root);
        getGAppsInstalled(root);
        return root;
    }

    public void getMyDevice(View root){
        final TextInputEditText input_vendor = root.findViewById(R.id.input_vendor);
        final TextInputEditText input_model = root.findViewById(R.id.input_model);
        final TextInputEditText input_cpu = root.findViewById(R.id.input_cpu);
        final TextInputEditText input_os = root.findViewById(R.id.input_os);
        GetDeviceInfo di = new GetDeviceInfo();
        input_vendor.setText(di.getVendor());
        input_vendor.setKeyListener(null);
        input_model.setText(di.getModel());
        input_model.setKeyListener(null);
        input_cpu.setText(di.getCPU());
        input_cpu.setKeyListener(null);
        input_os.setText(di.getOS());
        input_os.setKeyListener(null);
        SharedPreferences.Editor sp = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        sp.putString("vendor",di.getVendor());
        sp.putString("model",di.getModel());
        sp.putString("cpu",di.getCPU());
        sp.putString("os",di.getOS());
        sp.apply();
    }

    public void getOpenGAppsStatus(View root){
        final TextInputEditText input_opengapps_gapps_version = root.findViewById(R.id.input_opengapps_gapps_version);
        input_opengapps_gapps_version.setKeyListener(null);

        SharedPreferences.Editor spE =getActivity().getSharedPreferences("data", Context.MODE_PRIVATE).edit();
        SharedPreferences sp = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        if(sp.getString("opengapps","ERROR").equals("UNKNOWN")){
            //未检测

        }

    }

    public void getGAppsInstalled(View root){
        boolean stateofGApps = IsInstall.IsInstall(getActivity(), "com.google.android.gms");
        if(!stateofGApps){
            final MaterialCardView card_gapps_status = root.findViewById(R.id.card_gapps_status);
            final TextView text_device_gapps_status = root.findViewById(R.id.text_device_gapps_status);
            card_gapps_status.setStrokeColor(getResources().getColor(R.color.warning));
            text_device_gapps_status.setText(R.string.text_device_gms_no);
            text_device_gapps_status.setTextColor(getResources().getColor(R.color.warning));
            final LinearLayoutCompat layout_4 = root.findViewById(R.id.layout_4);
            layout_4.setVisibility(View.GONE);
        }
    }
}