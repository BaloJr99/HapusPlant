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
    private EditText etRegisterFamily;
    Button btnRegisterFamily;
    TextView tvRegisterFamilyVerification;
    AlertDialog dialog;
    Map<String, String> familyList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCollectionBinding.inflate(inflater, container, false);
        binding.btnAddFamily.setOnClickListener(view -> registerFamilyDialog());
        binding.btnRegisterSucculent.setOnClickListener(view -> registerSucculent());
        binding.ivAddSucculent.setOnClickListener(view -> takePicture());
        MediaManager.init(requireContext());
        fillSpinner();

        return binding.getRoot();
    }

    private void registerFamilyDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.register_family, null);
        builder.setView(view);

        btnRegisterFamily = view.findViewById(R.id.btnRegisterFamily);
        etRegisterFamily = view.findViewById(R.id.etRegisterFamily);
        tvRegisterFamilyVerification = view.findViewById(R.id.tvRegisterFamilyVerification);
        btnRegisterFamily.setOnClickListener(btnView -> addNewSucculentFamily());
        dialog = builder.create();
        dialog.show();
    }

    private void registerSucculent(){
        if(!emptyFields()){
            byte[] image = getImageBytes();
            uploadImage(image);
        }
    }

    private byte[] getImageBytes(){
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) binding.ivAddSucculent.getDrawable());
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void uploadImage(byte[] imageBytes){
        MediaManager.get().upload(imageBytes).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                System.out.println("STARTING...");
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                System.out.println("IN PROGRESS...");
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                createSucculent(Objects.requireNonNull(resultData.get("public_id")).toString());
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                System.out.println("ERROR...");
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                System.out.println("STARTING...");
            }
        }).dispatch();
    }
    
    private void addNewSucculentFamily(){
        if(!emptyDialogFields()){
            SucculentFamilyAPI familyAPI = RetrofitInstance.getRetrofitInstance().create(SucculentFamilyAPI.class);

            /* Get last known Token */
            HapusPlantLiteDb db = new HapusPlantLiteDb(getActivity());
            String token = db.getJwtIfExists();
            Call<Void> call = familyAPI.createFamily(new SucculentFamily(etRegisterFamily.getText().toString()), token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    dialog.dismiss();
                    fillSpinner();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(getActivity(), "An error ocurred", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean emptyFields(){
        boolean emptyFields = false;

        if(binding.ivAddSucculent.getDrawable().getConstantState().equals(Objects.requireNonNull(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_camera)).getConstantState())) {
            Toast.makeText(getActivity(), "The image is required", Toast.LENGTH_SHORT).show();
            emptyFields = true;
        }

        if(binding.etName.getText().toString().isEmpty()){
            binding.tvNameValidation.setVisibility(View.VISIBLE);
            emptyFields = true;
        }else{
            binding.tvNameValidation.setVisibility(View.GONE);
        }

        if(binding.spFamily.getSelectedItemPosition() == 0) {
            binding.tvFamilyVerification.setVisibility(View.VISIBLE);
            emptyFields = true;
        }else{
            binding.tvFamilyVerification.setVisibility(View.GONE);
        }
        return emptyFields;
    }

    private boolean emptyDialogFields(){
        if(etRegisterFamily.getText().toString().isEmpty()){
            tvRegisterFamilyVerification.setVisibility(View.VISIBLE);
            return true;
        }else{
            tvRegisterFamilyVerification.setVisibility(View.GONE);
        }
        return false;
    }

    private void fillSpinner(){
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
                familyList = new HashMap<>();
                if(!Objects.requireNonNull(response.body()).isEmpty()){
                    for (SucculentFamily sf: response.body()) {
                        families.add(sf.getFamily());
                        familyList.put(sf.getFamily(), sf.getIdSucculentFamily());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, families);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    binding.spFamily.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SucculentFamily>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void takePicture(){
        if(!checkCameraPermission()){
            requestCameraPermission();
        }else {
            PickImage();
        }
    }

    private void PickImage(){
        camaraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
    }

    ActivityResultLauncher<Intent> camaraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        Drawable d = new BitmapDrawable(getResources(), imageBitmap);
                        binding.ivAddSucculent.setImageDrawable(d);
                    }else{
                        binding.ivAddSucculent.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_camera));
                    }
                }
            });

    private void requestCameraPermission(){
        requestPermissions(new String[]{ Manifest.permission.CAMERA }, 100);
    }

    private boolean checkCameraPermission(){
        return ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void createSucculent(String url){
        try {
            SucculentKindAPI kindAPI = RetrofitInstance.getRetrofitInstanceWithBuilder().create(SucculentKindAPI.class);
            /* Get last known Token */
            HapusPlantLiteDb db = new HapusPlantLiteDb(getActivity());
            String token = db.getJwtIfExists();

            Call<Void> call = kindAPI.createSucculent(new SucculentType(
                    binding.etName.getText().toString(),
                    "",
                    url,
                    familyList.get(binding.spFamily.getSelectedItem().toString()),
                    binding.swEndemic.isChecked(),
                    binding.swPappers.isChecked(),
                    binding.swAlive.isChecked()), token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    try {
                        if(response.isSuccessful()){
                            Toast.makeText(getContext(), "Registered Succesfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "A unexpected error occurred", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex){
                        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex){
            Toast.makeText(getContext(), "An Error Occurred", Toast.LENGTH_SHORT).show();
        }
    }
}
