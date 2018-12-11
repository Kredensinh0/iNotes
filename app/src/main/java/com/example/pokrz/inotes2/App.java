package com.example.pokrz.inotes2;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }
}

