package com.example.audibayron.uiuc_events;

import android.app.Fragment;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TabHost;

/**
 * Created by Audi Bayron on 3/30/2015.
 */
public class MainTabs extends TabActivity {
    private TabHost mTabHost;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mTabHost.addTab(
                mTabHost.newTabSpec("tab1")
                        .setIndicator("Map")
                        .setContent(new Intent(this, MapsActivity.class))
        );
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2")
                        .setIndicator("Events")
                        .setContent(new Intent(this, MainActivity.class))
        );

    }

}
