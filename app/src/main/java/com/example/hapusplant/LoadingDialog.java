package com.example.hapusplant;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hapusplant.adapters.SharedCollectionAdapter;
import com.example.hapusplant.adapters.SucculentAdapter;
import com.example.hapusplant.database.HapusPlantLiteDb;
import com.example.hapusplant.interfaces.SharedContactsAPI;
import com.example.hapusplant.interfaces.SucculentKindAPI;
import com.example.hapusplant.models.SearchSucculentType;
import com.example.hapusplant.models.SharedCollectionContacts;
import com.example.hapusplant.network.RetrofitInstance;
import com.example.hapusplant.ui.home.HomeFragment;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity myActivity){
        activity = myActivity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    public void startLoadingSharingContacts(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.shared_collection_layout, null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        getAllContacts(dialogView);
        dialog = builder.create();
        dialog.show();
    }

    private void getAllContacts(View view){
        SharedContactsAPI contactsAPI = RetrofitInstance.getRetrofitInstance().create(SharedContactsAPI.class);

        /* Get last known Token */
        HapusPlantLiteDb db = new HapusPlantLiteDb(activity);
        String token = db.getJwtIfExists();
        Call<List<SharedCollectionContacts>> call = contactsAPI.getSharedContacts(token);
        call.enqueue(new Callback<List<SharedCollectionContacts>>() {
            @Override
            public void onResponse(@NonNull Call<List<SharedCollectionContacts>> call, @NonNull Response<List<SharedCollectionContacts>> response) {
                if(response.body() != null){
                    if(Objects.requireNonNull(response.body()).size() > 0){
                        RecyclerView rvSharedContacts = view.findViewById(R.id.rvSharedCollection);
                        rvSharedContacts.setLayoutManager(new LinearLayoutManager(view.getContext()));
                        SharedCollectionAdapter adapter = new SharedCollectionAdapter(response.body(), view.getContext());
                        rvSharedContacts.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SharedCollectionContacts>> call, @NonNull Throwable t) {
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
