package com.example.audibayron.uiuc_events;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

/**
 * Created by Audi Bayron on 3/30/2015.
 */
public class MainTabs extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_layout);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab1")
                        .setIndicator("Map", null),
                MapsActivity.class, null);
//        mTabHost.addTab(
//                mTabHost.newTabSpec("tab2")
//                        .setIndicator("Events", null),
//                MainActivity.class, null);
    }
}
