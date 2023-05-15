package com.example.hapusplant.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.download.picasso.PicassoDownloadRequestBuilderFactory;
import com.example.hapusplant.R;
import com.example.hapusplant.SucculentKindForm;
import com.example.hapusplant.database.HapusPlantLiteDb;
import com.example.hapusplant.holders.SharedCollectionHolder;
import com.example.hapusplant.holders.SucculentViewHolder;
import com.example.hapusplant.interfaces.SucculentKindAPI;
import com.example.hapusplant.models.SearchSucculentType;
import com.example.hapusplant.models.SharedCollectionContacts;
import com.example.hapusplant.network.RetrofitInstance;
import com.example.hapusplant.ui.home.HomeFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharedCollectionAdapter extends RecyclerView.Adapter<SharedCollectionHolder> {

    private List<SharedCollectionContacts> dataList;
    Context c;

    public SharedCollectionAdapter(List<SharedCollectionContacts> dataList, Context c){
        this.dataList = dataList;
        this.c = c;
    }

    @NonNull
    @Override
    public SharedCollectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_users, parent, false);
        return new SharedCollectionHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SharedCollectionHolder holder, int position) {
        holder.tvContactName.setText(dataList.get(position).getFullName());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
