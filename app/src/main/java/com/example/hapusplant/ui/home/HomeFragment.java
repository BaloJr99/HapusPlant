package com.example.hapusplant.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hapusplant.LoadingDialog;
import com.example.hapusplant.SucculentKindForm;
import com.example.hapusplant.adapters.SucculentAdapter;
import com.example.hapusplant.database.HapusPlantLiteDb;
import com.example.hapusplant.databinding.FragmentHomeBinding;
import com.example.hapusplant.interfaces.SucculentKindAPI;
import com.example.hapusplant.models.SearchSucculentType;
import com.example.hapusplant.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private SearchView searchView;
    private List<SearchSucculentType> succulentList;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        searchView = binding.searchView;
        swipeRefreshLayout = binding.swipeRefreshLayout;
        succulentList = new ArrayList<>();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getAllSuculents();
            swipeRefreshLayout.setRefreshing(false);
        });
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!succulentList.isEmpty()){
                    filterList(newText);
                }
                return false;
            }
        });
        binding.fab.setOnClickListener(this::moveToCollection);
        binding.fab.setImageTintMode(null);

        View root = binding.getRoot();

        getAllSuculents();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllSuculents();
    }

    private void moveToCollection(View view){
        startActivity(new Intent(getContext(), SucculentKindForm.class));
    }

    public void getAllSuculents(){
        LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoadingDialog();
        SucculentKindAPI kindAPI = RetrofitInstance.getRetrofitInstance().create(SucculentKindAPI.class);

        /* Get last known Token */
        HapusPlantLiteDb db = new HapusPlantLiteDb(getActivity());
        String token = db.getJwtIfExists();
        Call<List<SearchSucculentType>> call = kindAPI.getAllSucculents(token);
        call.enqueue(new Callback<List<SearchSucculentType>>() {
            @Override
            public void onResponse(@NonNull Call<List<SearchSucculentType>> call, @NonNull Response<List<SearchSucculentType>> response) {
                if(response.body() != null){
                    if(Objects.requireNonNull(response.body()).size() > 0){
                        binding.rvSucculents.setLayoutManager(new LinearLayoutManager(getActivity()));
                        succulentList = response.body();
                        succulentList.sort(Comparator.comparing(SearchSucculentType::getSucculentName));
                        SucculentAdapter adapter = new SucculentAdapter(succulentList, getContext(), HomeFragment.this);
                        binding.rvSucculents.setAdapter(adapter);
                    }
                }

                loadingDialog.dismissDialog();
            }

            @Override
            public void onFailure(@NonNull Call<List<SearchSucculentType>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                loadingDialog.dismissDialog();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void filterList(String text){
        List<SearchSucculentType> filteredList = new ArrayList<>();
        for (SearchSucculentType succulent: succulentList) {
            if(succulent.getSucculentName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(succulent);
            }
        }

        if(filteredList.isEmpty()){
            SucculentAdapter adapter = new SucculentAdapter(filteredList, getContext(), HomeFragment.this);
            binding.rvSucculents.setAdapter(adapter);
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }else {
            SucculentAdapter adapter = new SucculentAdapter(filteredList, getContext(), HomeFragment.this);
            binding.rvSucculents.setAdapter(adapter);
        }

    }
}
