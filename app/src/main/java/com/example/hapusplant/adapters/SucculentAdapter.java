package com.example.hapusplant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hapusplant.R;
import com.example.hapusplant.holders.SucculentViewHolder;
import com.example.hapusplant.models.SucculentType;

import java.util.ArrayList;
import java.util.List;

public class SucculentAdapter  extends RecyclerView.Adapter<SucculentViewHolder> {

    private List<SucculentType> dataList;

    public SucculentAdapter(List<SucculentType> dataList){
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public SucculentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_card, parent, false);
        return new SucculentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SucculentViewHolder holder, int position) {
        holder.tvSucculentName.setText(dataList.get(position).getKind());
        holder.swSucculentEndemic.setChecked(dataList.get(position).getEndemic());
        holder.swSucculentPappers.setChecked(dataList.get(position).getHasDocuments());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
