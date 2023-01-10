package com.example.greenstamp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.greenstamp.Adapters.InstalledAppsAdapter;
import com.example.greenstamp.Models.InstalledApp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<InstalledApp> installedApps = getInstalledApps();

        RecyclerView recyclerViewInstalledApps = findViewById(R.id.recyclerViewInstalledApps);
        recyclerViewInstalledApps.setLayoutManager(new LinearLayoutManager(this));

        InstalledAppsAdapter adapter = new InstalledAppsAdapter(installedApps);
        recyclerViewInstalledApps.setAdapter(adapter);
    }

    public List<InstalledApp> getInstalledApps() {
        List<PackageInfo> packageList = getPackageManager().getInstalledPackages(0);
        List<InstalledApp> installedApps = new ArrayList<>();

        for (int i=0; i < packageList.size(); i++) {
            PackageInfo packInfo = packageList.get(i);

            if(!((packInfo.applicationInfo.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP |
                    ApplicationInfo.FLAG_SYSTEM)) > 0)) {

                String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                String packageName = packInfo.packageName;
                Drawable appIcon = packInfo.applicationInfo.loadIcon(getPackageManager());
                long version = packInfo.getLongVersionCode();

                InstalledApp installedApp = new InstalledApp(appName, packageName, appIcon, version);
                installedApps.add(installedApp);
            }
        }

        return installedApps;
    }

}