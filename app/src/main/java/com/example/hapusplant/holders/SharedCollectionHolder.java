package com.example.hapusplant.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hapusplant.R;

public class SharedCollectionHolder extends RecyclerView.ViewHolder {
    public TextView tvContactName;
    public ImageView ivProfilePhoto;

    public SharedCollectionHolder(View itemView) {
        super(itemView);
        tvContactName = itemView.findViewById(R.id.tvContactName);
        ivProfilePhoto = itemView.findViewById(R.id.ivProfilePhoto);
    }
}
