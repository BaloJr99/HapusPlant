package com.example.hapusplant;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.download.picasso.PicassoDownloadRequestBuilderFactory;
import com.example.hapusplant.database.HapusPlantLiteDb;
import com.example.hapusplant.interfaces.SucculentFamilyAPI;
import com.example.hapusplant.interfaces.SucculentKindAPI;
import com.example.hapusplant.models.SucculentFamily;
import com.example.hapusplant.models.SucculentType;
import com.example.hapusplant.network.RetrofitInstance;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SucculentKindForm extends AppCompatActivity {

    EditText etRegisterFamily, etName;
    Button btnRegisterFamily, btnRegisterSucculent;
    ImageButton btnAddFamily;
    TextView tvRegisterFamilyVerification, tvNameValidation, tvFamilyVerification;
    Spinner spFamily;
    AlertDialog dialog;
    Map<String, String> familyList;
    List<String> familyNames;
    ImageView ivAddSucculent;
    Switch swEndemic, swPappers, swAlive;
    Boolean hasInfo, imageChanged = false;
    String idSucculent;
    SucculentType succulentTypeToEdit;
    LoadingDialog loadingDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succulent_kind_form);

        loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoagingDialog();

        btnAddFamily = findViewById(R.id.btnAddFamily);
        btnRegisterSucculent = findViewById(R.id.btnRegisterSucculent);
        ivAddSucculent = findViewById(R.id.ivAddSucculent);
        etName = findViewById(R.id.etName);
        tvNameValidation = findViewById(R.id.tvNameValidation);
        tvFamilyVerification = findViewById(R.id.tvFamilyVerification);
        spFamily = findViewById(R.id.spFamily);
        swEndemic = findViewById(R.id.swEndemic);
        ivAddSucculent = findViewById(R.id.ivAddSucculent);
        swPappers = findViewById(R.id.swPappers);
        swAlive = findViewById(R.id.swAlive);

        btnAddFamily.setOnClickListener(view -> registerFamilyDialog());
        btnRegisterSucculent.setOnClickListener(view -> registerSucculent());
        ivAddSucculent.setOnClickListener(view -> takePicture());
        fillSpinner();

        Bundle bundles = getIntent().getExtras();
        if(bundles != null){
            hasInfo = true;
            idSucculent = bundles.getString("idSucculent");
            btnRegisterSucculent.setText("EDIT");
        }else {
            hasInfo = false;
        }
    }

    private void registerFamilyDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        loadingDialog.startLoagingDialog();
        if(!emptyFields()){
            if(hasInfo){
                if (imageChanged){
                    byte[] image = getImageBytes();
                    uploadImage(image);
                }else {
                    editSucculent(succulentTypeToEdit.getPhotoLink());
                }
            }else {
                byte[] image = getImageBytes();
                uploadImage(image);
            }
        }else{
            loadingDialog.dismissDialog();
        }
    }

    private byte[] getImageBytes(){
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) ivAddSucculent.getDrawable());
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
                if(hasInfo){
                    editSucculent(Objects.requireNonNull(resultData.get("public_id")).toString());
                }else {
                    createSucculent(Objects.requireNonNull(resultData.get("public_id")).toString());
                }
                loadingDialog.dismissDialog();
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                System.out.println(error);
                loadingDialog.dismissDialog();
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                System.out.println("STARTING...");
            }
        }).dispatch();
    }

    private void addNewSucculentFamily(){
        loadingDialog.startLoagingDialog();
        if(!emptyDialogFields()){
            SucculentFamilyAPI familyAPI = RetrofitInstance.getRetrofitInstance().create(SucculentFamilyAPI.class);

            /* Get last known Token */
            HapusPlantLiteDb db = new HapusPlantLiteDb(this);
            String token = db.getJwtIfExists();
            Call<Void> call = familyAPI.createFamily(new SucculentFamily(etRegisterFamily.getText().toString()), token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    loadingDialog.dismissDialog();
                    dialog.dismiss();
                    fillSpinner();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    loadingDialog.dismissDialog();
                    Toast.makeText(SucculentKindForm.this, "An error ocurred", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            loadingDialog.dismissDialog();
        }
    }

    private boolean emptyFields(){
        boolean emptyFields = false;

        if(ivAddSucculent.getDrawable().getConstantState().equals(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.ic_camera)).getConstantState())) {
            Toast.makeText(this, "The image is required", Toast.LENGTH_SHORT).show();
        }

        if(etName.getText().toString().isEmpty()){
            tvNameValidation.setVisibility(View.VISIBLE);
            emptyFields = true;
        }else{
            tvNameValidation.setVisibility(View.GONE);
        }

        if(spFamily.getSelectedItemPosition() == 0) {
            tvFamilyVerification.setVisibility(View.VISIBLE);
            emptyFields = true;
        }else{
            tvFamilyVerification.setVisibility(View.GONE);
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
        HapusPlantLiteDb db = new HapusPlantLiteDb(this);
        String token = db.getJwtIfExists();

        /* Call the method with parameter in the interface to get the notice data*/
        Call<List<SucculentFamily>> call = familyAPI.getFamilies(token);

        call.enqueue(new Callback<List<SucculentFamily>>() {
            @Override
            public void onResponse(@NonNull Call<List<SucculentFamily>> call, @NonNull Response<List<SucculentFamily>> response) {
                List<String> families = new ArrayList<String>(){{ add("Select...");}};
                familyList = new HashMap<>();
                familyNames = new ArrayList<>();
                if(!Objects.requireNonNull(response.body()).isEmpty()){
                    for (SucculentFamily sf: response.body()) {
                        families.add(sf.getFamily());
                        familyList.put(sf.getFamily(), sf.getIdSucculentFamily());
                        familyNames.add(sf.getIdSucculentFamily());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SucculentKindForm.this, R.layout.spinner_item, families);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spFamily.setAdapter(adapter);

                    if(hasInfo){
                        fillForm(idSucculent);
                    }else{
                        loadingDialog.dismissDialog();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SucculentFamily>> call, @NonNull Throwable t) {
                Toast.makeText(SucculentKindForm.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
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
                        ivAddSucculent.setImageDrawable(d);
                        imageChanged = true;
                    }else{
                        if(!hasInfo){
                            ivAddSucculent.setImageDrawable(ContextCompat.getDrawable(SucculentKindForm.this, R.drawable.ic_camera));
                        }
                    }
                }
            });

    private void requestCameraPermission(){
        requestPermissions(new String[]{ android.Manifest.permission.CAMERA }, 100);
    }

    private boolean checkCameraPermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void createSucculent(String url){
        try {
            SucculentKindAPI kindAPI = RetrofitInstance.getRetrofitInstanceWithBuilder().create(SucculentKindAPI.class);
            /* Get last known Token */
            HapusPlantLiteDb db = new HapusPlantLiteDb(this);
            String token = db.getJwtIfExists();

            Call<Void> call = kindAPI.createSucculent(new SucculentType(
                    etName.getText().toString(),
                    "",
                    url,
                    familyList.get(spFamily.getSelectedItem().toString()),
                    swEndemic.isChecked(),
                    swPappers.isChecked(),
                    swAlive.isChecked()), token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    try {
                        if(response.isSuccessful()){
                            Toast.makeText(SucculentKindForm.this, "Registered Succesfully", Toast.LENGTH_SHORT).show();
                            clearFields();
                        }else {
                            Toast.makeText(SucculentKindForm.this, "A unexpected error occurred", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex){
                        Toast.makeText(SucculentKindForm.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismissDialog();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, Throwable t) {
                    Toast.makeText(SucculentKindForm.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
                }
            });
        } catch (Exception ex){
            Toast.makeText(SucculentKindForm.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
            loadingDialog.dismissDialog();
        }
    }

    private void editSucculent(String url){

        try {
            SucculentKindAPI kindAPI = RetrofitInstance.getRetrofitInstanceWithBuilder().create(SucculentKindAPI.class);
            /* Get last known Token */
            HapusPlantLiteDb db = new HapusPlantLiteDb(this);
            String token = db.getJwtIfExists();

            Call<Void> call = kindAPI.editSucculent(new SucculentType(
                    succulentTypeToEdit.getIdSucculent(),
                    etName.getText().toString(),
                    "",
                    url,
                    familyList.get(spFamily.getSelectedItem().toString()),
                    swEndemic.isChecked(),
                    swPappers.isChecked(),
                    swAlive.isChecked()), token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    try {
                        if(response.isSuccessful()){
                            Toast.makeText(SucculentKindForm.this, "Edited Succesfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(SucculentKindForm.this, "A unexpected error occurred", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex){
                        Toast.makeText(SucculentKindForm.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismissDialog();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, Throwable t) {
                    Toast.makeText(SucculentKindForm.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismissDialog();
                }
            });
        } catch (Exception ex){
            Toast.makeText(SucculentKindForm.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
            loadingDialog.dismissDialog();
        }
    }

    private void clearFields(){
        spFamily.setSelection(0);
        etName.setText("");
        swEndemic.setChecked(false);
        swPappers.setChecked(false);
        swAlive.setChecked(true);
    }

    private void fillForm(String idSucculent){
        try {
            SucculentKindAPI kindAPI = RetrofitInstance.getRetrofitInstanceWithBuilder().create(SucculentKindAPI.class);
            /* Get last known Token */
            HapusPlantLiteDb db = new HapusPlantLiteDb(this);
            String token = db.getJwtIfExists();

            Call<SucculentType> call = kindAPI.getSucculentKindById(idSucculent, token);
            call.enqueue(new Callback<SucculentType>() {
                @Override
                public void onResponse(@NonNull Call<SucculentType> call, @NonNull Response<SucculentType> response) {
                    succulentTypeToEdit = response.body();
                    int index = familyNames.indexOf(Objects.requireNonNull(succulentTypeToEdit).getIdSucculentFamily());
                    etName.setText(Objects.requireNonNull(succulentTypeToEdit).getKind());
                    swAlive.setChecked(succulentTypeToEdit.getAlive());
                    swPappers.setChecked(succulentTypeToEdit.getHasDocuments());
                    swEndemic.setChecked(succulentTypeToEdit.getEndemic());
                    spFamily.setSelection(++index);
                    MediaManager.get().setDownloadRequestBuilderFactory(new PicassoDownloadRequestBuilderFactory());
                    MediaManager.get().download(SucculentKindForm.this).load(succulentTypeToEdit.getPhotoLink()).into(ivAddSucculent);
                    loadingDialog.dismissDialog();
                }

                @Override
                public void onFailure(@NonNull Call<SucculentType> call, @NonNull Throwable t) {
                    loadingDialog.dismissDialog();
                }
            });
        } catch (Exception ex){
            Toast.makeText(SucculentKindForm.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
            loadingDialog.dismissDialog();
        }
    }
}