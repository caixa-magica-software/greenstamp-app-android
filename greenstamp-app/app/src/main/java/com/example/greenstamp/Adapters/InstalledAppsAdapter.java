package com.example.greenstamp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenstamp.Models.InstalledApp;
import com.example.greenstamp.R;

import java.util.List;

public class InstalledAppsAdapter extends RecyclerView.Adapter<InstalledAppsViewHolder> {
    Context context;
    List<InstalledApp> installedAppList;

    public InstalledAppsAdapter(Context context, List<InstalledApp> installedAppList) {
        this.context = context;
        this.installedAppList = installedAppList;
    }

    @NonNull
    @Override
    public InstalledAppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.installed_app_card, parent,false);
        return new InstalledAppsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstalledAppsViewHolder holder, int position) {
        holder.imageViewInstalledApp.setImageDrawable(installedAppList.get(position).appIcon);
        holder.textViewInstalledApp.setText(installedAppList.get(position).appName);
    }

    @Override
    public int getItemCount() {
        return installedAppList.size();
    }
}