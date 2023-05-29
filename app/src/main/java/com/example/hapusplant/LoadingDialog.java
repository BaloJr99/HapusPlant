package com.example.hapusplant;

import android.app.Activity;
import android.app.AlertDialog;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hapusplant.adapters.SharedCollectionAdapter;
import com.example.hapusplant.adapters.SucculentAdapter;
import com.example.hapusplant.database.HapusPlantLiteDb;
import com.example.hapusplant.interfaces.ProfileAPI;
import com.example.hapusplant.interfaces.SharedContactsAPI;
import com.example.hapusplant.interfaces.SucculentKindAPI;
import com.example.hapusplant.models.SearchSucculentType;
import com.example.hapusplant.models.SharedCollectionContacts;
import com.example.hapusplant.network.RetrofitInstance;
import com.example.hapusplant.ui.home.HomeFragment;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoadingDialog {
    private final Activity activity;
    private AlertDialog dialog;
    private List<SharedCollectionContacts> listOfContacts;

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
        SearchView searchView = dialogView.findViewById(R.id.search_shared);
        LinearLayout llSharedContacts = dialogView.findViewById(R.id.llSharedContacts);
        RecyclerView rvSharedContacts = dialogView.findViewById(R.id.rvSharedCollection);
        ImageView ivIcon = dialogView.findViewById(R.id.ivIcon);
        LinearLayout llNotSharedContacts = dialogView.findViewById(R.id.llNotSharedContacts);
        RecyclerView rvNotSharedContacts = dialogView.findViewById(R.id.rvNotSharedCollection);
        ImageView ivIconNotShared = dialogView.findViewById(R.id.ivIconNotShared);
        llSharedContacts.setOnClickListener(view -> {
            TransitionManager.beginDelayedTransition(rvSharedContacts, new AutoTransition());
            int v = (rvSharedContacts.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
            if(v == View.GONE){
                ivIcon.setImageResource(R.drawable.arrowup);
            }else {
                ivIcon.setImageResource(R.drawable.arrowdown);
            }
            rvSharedContacts.setVisibility(v);
        });

        llNotSharedContacts.setOnClickListener(view -> {
            TransitionManager.beginDelayedTransition(rvNotSharedContacts, new AutoTransition());
            int v = (rvNotSharedContacts.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
            if(v == View.GONE){
                ivIconNotShared.setImageResource(R.drawable.arrowup);
            }else {
                ivIconNotShared.setImageResource(R.drawable.arrowdown);
            }
            rvNotSharedContacts.setVisibility(v);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!s.isEmpty()){
                    /* Get last known Token */
                    HapusPlantLiteDb db = new HapusPlantLiteDb(activity);
                    String token = db.getJwtIfExists();

                    Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
                    ProfileAPI profileAPI = retrofit.create(ProfileAPI.class);
                    Call<List<SharedCollectionContacts>> call = profileAPI.getMatchingUsername(s, token);

                    call.enqueue(new Callback<List<SharedCollectionContacts>>() {
                        @Override
                        public void onResponse(Call<List<SharedCollectionContacts>> call, Response<List<SharedCollectionContacts>> response) {
                            if(response.body() != null){
                                List<SharedCollectionContacts> listOfNotContacts = response.body();
                                List<String> listIdOfContacts = listOfContacts.stream().map(SharedCollectionContacts::getIdUser).collect(Collectors.toList());
                                listOfNotContacts.removeIf(x -> listIdOfContacts.contains(x.getIdUser()));
                                RecyclerView rvSharedContacts = dialogView.findViewById(R.id.rvNotSharedCollection);
                                rvSharedContacts.setLayoutManager(new LinearLayoutManager(dialogView.getContext()));
                                SharedCollectionAdapter adapter = new SharedCollectionAdapter(listOfNotContacts, dialogView.getContext());
                                rvSharedContacts.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SharedCollectionContacts>> call, Throwable t) {

                        }
                    });
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

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
                        listOfContacts = response.body();
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
