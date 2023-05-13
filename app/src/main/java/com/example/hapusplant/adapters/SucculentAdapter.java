package com.example.hapusplant.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.download.picasso.PicassoDownloadRequestBuilderFactory;
import com.example.hapusplant.R;
import com.example.hapusplant.SucculentKindForm;
import com.example.hapusplant.database.HapusPlantLiteDb;
import com.example.hapusplant.holders.SucculentViewHolder;
import com.example.hapusplant.interfaces.SucculentFamilyAPI;
import com.example.hapusplant.models.SearchSucculentType;
import com.example.hapusplant.models.SucculentFamily;
import com.example.hapusplant.models.SucculentType;
import com.example.hapusplant.network.RetrofitInstance;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SucculentAdapter  extends RecyclerView.Adapter<SucculentViewHolder> {

    private List<SearchSucculentType> dataList;
    Context c;

    public SucculentAdapter(List<SearchSucculentType> dataList, Context c){
        this.dataList = dataList;
        this.c = c;
    }

    @NonNull
    @Override
    public SucculentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_card, parent, false);
        return new SucculentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SucculentViewHolder holder, int position) {
        holder.swSucculentEndemic.setChecked(dataList.get(position).getEndemic());
        holder.swSucculentPappers.setChecked(dataList.get(position).getHasDocuments());
        holder.tvSucculentName.setText(dataList.get(position).getSucculentFamily().concat(" ").concat(dataList.get(position).getKind()));
        MediaManager.get().setDownloadRequestBuilderFactory(new PicassoDownloadRequestBuilderFactory());
        MediaManager.get().download(c).load(dataList.get(position).getPhotoLink()).into(holder.ivSucculentPhoto);

        holder.btnEdit.setOnClickListener(view -> {
            Intent intent = new Intent(c, SucculentKindForm.class);
            intent.putExtra("idSucculent", dataList.get(position).getIdSucculent());
            c.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(view -> {

        });

        holder.btnDownload.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
