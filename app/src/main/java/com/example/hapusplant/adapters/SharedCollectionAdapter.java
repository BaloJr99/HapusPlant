package com.example.hapusplant.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.example.hapusplant.interfaces.SharedContactsAPI;
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

    private final List<SharedCollectionContacts> dataList;
    Context c;
    private boolean addingContact;

    public SharedCollectionAdapter(List<SharedCollectionContacts> dataList, Context c, boolean addingContact){
        this.dataList = dataList;
        this.c = c;
        this.addingContact = addingContact;
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
        if(addingContact){
            holder.btnDeleteSharedCollection.setImageResource(R.drawable.ic_add_contact);
            holder.btnDeleteSharedCollection.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8DD604")));
            holder.btnDeleteSharedCollection.setOnClickListener(view -> {
                SharedContactsAPI contactsAPI = RetrofitInstance.getRetrofitInstance().create(SharedContactsAPI.class);

                /* Get last known Token */
                HapusPlantLiteDb db = new HapusPlantLiteDb(c);
                String token = db.getJwtIfExists();
                Call<Void> call = contactsAPI.addNewSharedUser(dataList.get(position).getIdUser(), token);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        notifyDataSetChanged();
                        Toast.makeText(c, "Added Successfully", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(c, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }else {
            holder.btnDeleteSharedCollection.setOnClickListener(view -> {
                SharedContactsAPI contactsAPI = RetrofitInstance.getRetrofitInstance().create(SharedContactsAPI.class);

                /* Get last known Token */
                HapusPlantLiteDb db = new HapusPlantLiteDb(c);
                String token = db.getJwtIfExists();
                Call<Void> call = contactsAPI.deleteSharedContact(dataList.get(position).getIdUser(), token);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        dataList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(c, "Delete Successfully", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(c, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });

            });
        }

        if(dataList.get(position).getPhoto() != null){
            MediaManager.get().setDownloadRequestBuilderFactory(new PicassoDownloadRequestBuilderFactory());
            MediaManager.get().download(c).load(dataList.get(position).getPhoto()).into(holder.ivProfilePhoto);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
