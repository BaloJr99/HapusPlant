package com.example.hapusplant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;

import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.utils.ObjectUtils;
import com.example.hapusplant.interfaces.ProfileAPI;
import com.example.hapusplant.models.NewUser;
import com.example.hapusplant.models.ProfileModel;
import com.example.hapusplant.models.UserModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    EditText etBirthdate, etUsername, etPassword, etFirstName, etLastName;
    DatePickerDialog picker;
    Button btnRegister;
    ImageButton imgBtnProfile;
    String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        MediaManager.init(this);

        etBirthdate = findViewById(R.id.etBirthdate);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etFirstName = findViewById(R.id.etName);
        etLastName = findViewById(R.id.etLastName);
        btnRegister = findViewById(R.id.btnRegister);
        imgBtnProfile = findViewById(R.id.btnProfilePhoto);

        etBirthdate.setOnClickListener(view -> showDatePicker());
        btnRegister.setOnClickListener(view -> registerUser());
    }

    private void showDatePicker(){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> etBirthdate.setText(getString(R.string.dateformat, selectedYear, (selectedMonth + 1) , selectedDay)), year, month, day);
        picker.show();
    }

    private void registerUser(){
        byte[] image = getImageBytes();
        uploadImage(image);
    }

    private byte[] getImageBytes(){
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) imgBtnProfile.getDrawable());
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
                createUser(Objects.requireNonNull(resultData.get("public_id")).toString());
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

    private void createUser(String url){
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(String.class, new EmptyStringSerializer()).create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.113:5154/")
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();

            if(url.isEmpty()){
                Toast.makeText(this, "An error ocurred", Toast.LENGTH_SHORT).show();
            }else {
                ProfileAPI profileAPI = retrofit.create(ProfileAPI.class);
                @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(etBirthdate.getText().toString()));
                Call<Void> call = profileAPI.createUser(
                        new NewUser(
                                new ProfileModel("",
                                        etFirstName.getText().toString(),
                                        etLastName.getText().toString(),
                                        url,
                                        date
                                ),
                                new UserModel(
                                        etUsername.getText().toString(),
                                        etPassword.getText().toString(),
                                        "user",
                                        true)));
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        try {
                            if(response.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                Toast.makeText(RegisterActivity.this, "Registered Succesfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(RegisterActivity.this, "A unexpected error occurred", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex){
                            Toast.makeText(RegisterActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception ex){
            Toast.makeText(this, "An Error Occurred", Toast.LENGTH_SHORT).show();
        }
    }
}