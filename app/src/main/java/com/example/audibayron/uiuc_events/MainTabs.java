package com.example.audibayron.uiuc_events;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainTabs extends TabActivity {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        TabHost mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mTabHost.addTab(
                mTabHost.newTabSpec("tab1")
                        .setIndicator("Map")
                        .setContent(new Intent(this, Map.class))
        );
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2")
                        .setIndicator("Events")
                        .setContent(new Intent(this, MainActivity.class))
        );

    }

}
