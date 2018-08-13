package com.jakting.opengapps;

import com.jakting.opengapps.utils.AllInAll;
import com.jakting.opengapps.utils.FirstRun;
import com.jakting.opengapps.utils.GetLatest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.app_name);
        Context context = this.getActivity();
        View view = this.getView();
        //程序启动时执行所需方法
        CardView do_card = (CardView) view.findViewById(R.id.state_latest);
        do_card.setClickable(false);
        AllInAll.allinall(context,view);
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

}