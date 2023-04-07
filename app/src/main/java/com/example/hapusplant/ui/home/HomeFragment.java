package com.example.hapusplant.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hapusplant.R;
import com.example.hapusplant.adapters.SucculentAdapter;
import com.example.hapusplant.database.HapusPlantLiteDb;
import com.example.hapusplant.databinding.FragmentHomeBinding;
import com.example.hapusplant.interfaces.SucculentKindAPI;
import com.example.hapusplant.models.SucculentType;
import com.example.hapusplant.network.RetrofitInstance;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.fab.setOnClickListener(this::moveToCollection);
        View root = binding.getRoot();

        getAllSuculents();
        return root;
    }

    private void moveToCollection(View view){
        Navigation.findNavController(view).navigate(R.id.nav_collection);
    }

    private void getAllSuculents(){
        SucculentKindAPI kindAPI = RetrofitInstance.getRetrofitInstance().create(SucculentKindAPI.class);

        /* Get last known Token */
        HapusPlantLiteDb db = new HapusPlantLiteDb(getActivity());
        String token = db.getJwtIfExists();
        Call<List<SucculentType>> call = kindAPI.getAllSucculents(token);
        call.enqueue(new Callback<List<SucculentType>>() {
            @Override
            public void onResponse(@NonNull Call<List<SucculentType>> call, @NonNull Response<List<SucculentType>> response) {
                if(Objects.requireNonNull(response.body()).size() > 0){
                    binding.rvSucculents.setLayoutManager(new LinearLayoutManager(getActivity()));
                    SucculentAdapter adapter = new SucculentAdapter(response.body());
                    binding.rvSucculents.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SucculentType>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
