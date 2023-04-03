package com.example.hapusplant.ui.collection;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hapusplant.HomeActivity;
import com.example.hapusplant.database.HapusPlantLiteDb;
import com.example.hapusplant.databinding.FragmentCollectionBinding;
import com.example.hapusplant.interfaces.SucculentFamilyAPI;
import com.example.hapusplant.models.SucculentFamily;
import com.example.hapusplant.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionFragment extends Fragment {
    private FragmentCollectionBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCollectionBinding.inflate(inflater, container, false);
        /* Create handle for the RetrofitInstance interface*/
        SucculentFamilyAPI familyAPI = RetrofitInstance.getRetrofitInstance().create(SucculentFamilyAPI.class);

        /* Get last known Token */
        HapusPlantLiteDb db = new HapusPlantLiteDb(getActivity());
        String token = db.getJwtIfExists();

        /* Call the method with parameter in the interface to get the notice data*/
        Call<List<SucculentFamily>> call = familyAPI.getFamilies(token);

        call.enqueue(new Callback<List<SucculentFamily>>() {
            @Override
            public void onResponse(@NonNull Call<List<SucculentFamily>> call, @NonNull Response<List<SucculentFamily>> response) {
                List<String> families = new ArrayList<String>(){{ add("Select...");}};
                if(!Objects.requireNonNull(response.body()).isEmpty()){
                    for (SucculentFamily sf: response.body()) {
                        families.add(sf.getFamily());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, families);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spFamily.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SucculentFamily>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
