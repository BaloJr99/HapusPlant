package com.example.hapusplant;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.download.picasso.PicassoDownloadRequestBuilderFactory;
import com.example.hapusplant.database.HapusPlantLiteDb;
import com.example.hapusplant.interfaces.ProfileAPI;
import com.example.hapusplant.models.ProfileModel;
import com.example.hapusplant.network.RetrofitInstance;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hapusplant.databinding.ActivityHomeBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    HapusPlantLiteDb hapusPlantLiteDb;
    LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if(MediaManager.get() == null){
                MediaManager.init(this);
            }
        }catch (Exception ex){
            MediaManager.init(this);
        }

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.mainBar.toolbar);

        DrawerLayout drawer = binding.drawerHome;
        NavigationView navigationView = binding.homeNav;
        View headerLayout = navigationView.getHeaderView(0);
        fillProfileData(headerLayout);
        navigationView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_collection, R.id.nav_wishlist, R.id.nav_profile, R.id.nav_close)
                .setOpenableLayout(drawer)
                .build();
        navigationView.getMenu().findItem(R.id.nav_close).setOnMenuItemClickListener(menuItem -> {
            hapusPlantLiteDb = new HapusPlantLiteDb(this);
            hapusPlantLiteDb.deleteJwt();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        });
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        dialog = new LoadingDialog(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            dialog.startLoadingSharingContacts();
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillProfileData(View v){
        ImageView imageView = v.findViewById(R.id.ivProfileDrawerPhoto);
        TextView textView = v.findViewById(R.id.tvNameProfile);
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        ProfileAPI profileAPI = retrofit.create(ProfileAPI.class);

        /* Get last known Token */
        HapusPlantLiteDb db = new HapusPlantLiteDb(getApplicationContext());
        String token = db.getJwtIfExists();

        Call<ProfileModel> call = profileAPI.getProfileData(token);

        call.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                ProfileModel profileModel = response.body();
                textView.setText(profileModel.getName() + " " + profileModel.getLastName());
                if(profileModel.getPhoto() != null){
                    MediaManager.get().setDownloadRequestBuilderFactory(new PicassoDownloadRequestBuilderFactory());
                    MediaManager.get().download(HomeActivity.this).load(profileModel.getPhoto()).into(imageView);
                }
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {

            }
        });
        imageView.setImageResource(R.drawable.ic_profile);
    }
}