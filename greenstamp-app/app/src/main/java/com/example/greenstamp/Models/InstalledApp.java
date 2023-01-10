package com.example.greenstamp.Models;

import android.graphics.drawable.Drawable;

public class InstalledApp {
    public String appName;
    public String packageName;
    public Drawable appIcon;
    public long version;

    public InstalledApp(String appName, String packageName, Drawable appIcon, long version) {
        this.appName = appName;
        this.packageName = packageName;
        this.appIcon = appIcon;
        this.version = version;
    }
}