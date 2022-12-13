package com.example.greenstamp.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenstamp.R;

public class InstalledAppsViewHolder extends RecyclerView.ViewHolder {
    ImageView imageViewInstalledApp;
    TextView textViewInstalledApp;
    CardView cardViewInstalledApp;

    public InstalledAppsViewHolder(@NonNull View itemView) {
        super(itemView);

        imageViewInstalledApp = itemView.findViewById(R.id.imageViewInstalledApp);
        textViewInstalledApp = itemView.findViewById(R.id.textViewInstalledApp);
        cardViewInstalledApp = itemView.findViewById(R.id.cardViewInstalledApp);
    }
}