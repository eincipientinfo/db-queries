package com.incipientinfo.dbqueries.common;

import android.app.Application;

public class CustomFontApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "SERIF", "Poppins-Medium.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "Pacifico-Regular.ttf");

    }
}