package com.example.hapusplant.adapters;

import android.content.Context;
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
import com.example.hapusplant.database.HapusPlantLiteDb;
import com.example.hapusplant.holders.SucculentViewHolder;
import com.example.hapusplant.interfaces.SucculentFamilyAPI;
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

    private List<SucculentType> dataList;
    Context c;

    public SucculentAdapter(List<SucculentType> dataList, Context c){
        try {
            MediaManager.init(c);
        }catch (Exception ex){}
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
        MediaManager.get().setDownloadRequestBuilderFactory(new PicassoDownloadRequestBuilderFactory());
        MediaManager.get().download(c).load(dataList.get(position).getPhotoLink()).into(holder.ivSucculentPhoto);
        SucculentFamilyAPI familyAPI = RetrofitInstance.getRetrofitInstance().create(SucculentFamilyAPI.class);
        /* Get last known Token */
        HapusPlantLiteDb db = new HapusPlantLiteDb(c);
        String token = db.getJwtIfExists();
        Call<SucculentFamily> call = familyAPI.getFamilyById(dataList.get(position).getIdSucculentFamily(), token);
        call.enqueue(new Callback<SucculentFamily>() {
            @Override
            public void onResponse(@NonNull Call<SucculentFamily> call, @NonNull Response<SucculentFamily> response) {
                holder.tvSucculentName.setText(Objects.requireNonNull(response.body()).getFamily().concat(" ").concat(dataList.get(position).getKind()));
            }

            @Override
            public void onFailure(@NonNull Call<SucculentFamily> call, @NonNull Throwable t) {

            }
        });

        holder.btnEdit.setOnClickListener(view -> {

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
