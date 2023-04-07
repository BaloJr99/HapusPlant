package com.example.hapusplant.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hapusplant.R;

public class SucculentViewHolder extends RecyclerView.ViewHolder {
    public TextView tvSucculentName, tvSucculentFamilyName;
    public Switch swSucculentEndemic, swSucculentPappers;
    public ImageView ivSucculentPhoto;

    public SucculentViewHolder(View itemView) {
        super(itemView);
        tvSucculentName = itemView.findViewById(R.id.tvSucculentName);
        tvSucculentFamilyName = itemView.findViewById(R.id.tvSucculentFamilyName);
        swSucculentEndemic = itemView.findViewById(R.id.swSucculentEndemic);
        swSucculentPappers = itemView.findViewById(R.id.swSucculentPappers);
        ivSucculentPhoto = itemView.findViewById(R.id.ivSucculentPhoto);
    }
}
