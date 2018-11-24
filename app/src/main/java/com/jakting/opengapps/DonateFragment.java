package com.jakting.opengapps;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class DonateFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_donate, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.donate);
        clickAlipay();
        clickGP();
    }



    private void clickAlipay(){
        LinearLayout alipay = (LinearLayout)getActivity().findViewById(R.id.alipay);
        alipay.setOnClickListener(this);
    }
    private void clickGP(){
        LinearLayout gp = (LinearLayout)getActivity().findViewById(R.id.google_play);
        gp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alipay:
                Uri alipay_link = Uri.parse("https://qr.alipay.com/a6x09739oi8tn3iretpt0ef");
                startActivity(new Intent(Intent.ACTION_VIEW,alipay_link));
                break;
            case R.id.google_play:
                Uri gp_link = Uri.parse("market://details?id=com.jakting.opengapps.donation");
                startActivity(new Intent(Intent.ACTION_VIEW,gp_link));
                break;
            default:
                break;
        }
    }
}
