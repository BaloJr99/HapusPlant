package com.example.hapusplant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hapusplant.R;
import com.example.hapusplant.holders.SucculentViewHolder;
import com.example.hapusplant.models.SucculentFamily;

import java.util.ArrayList;

public class SucculentAdapter  extends RecyclerView.Adapter<SucculentViewHolder> {

    private ArrayList<SucculentFamily> dataList;

    public SucculentAdapter(ArrayList<SucculentFamily> dataList){
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public SucculentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_home, parent, false);
        return new SucculentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SucculentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
