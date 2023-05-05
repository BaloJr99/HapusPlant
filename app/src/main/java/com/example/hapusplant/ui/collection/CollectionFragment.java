package com.example.hapusplant.ui.collection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.hapusplant.EmptyStringSerializer;
import com.example.hapusplant.HomeActivity;
import com.example.hapusplant.LoginActivity;
import com.example.hapusplant.R;
import com.example.hapusplant.RegisterActivity;
import com.example.hapusplant.database.HapusPlantLiteDb;
import com.example.hapusplant.databinding.FragmentCollectionBinding;
import com.example.hapusplant.interfaces.ProfileAPI;
import com.example.hapusplant.interfaces.SucculentFamilyAPI;
import com.example.hapusplant.interfaces.SucculentKindAPI;
import com.example.hapusplant.models.NewUser;
import com.example.hapusplant.models.ProfileModel;
import com.example.hapusplant.models.SucculentFamily;
import com.example.hapusplant.models.SucculentType;
import com.example.hapusplant.models.UserModel;
import com.example.hapusplant.network.RetrofitInstance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CollectionFragment extends Fragment {
    private FragmentCollectionBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCollectionBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}
