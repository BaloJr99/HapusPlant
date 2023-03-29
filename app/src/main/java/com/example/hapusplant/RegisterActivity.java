package com.example.hapusplant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hapusplant.interfaces.ProfileAPI;
import com.example.hapusplant.models.NewUser;
import com.example.hapusplant.models.ProfileModel;
import com.example.hapusplant.models.UserModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    EditText etBirthdate, etUsername, etPassword, etFirstName, etLastName;
    DatePickerDialog picker;
    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etBirthdate = findViewById(R.id.etBirthdate);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etFirstName = findViewById(R.id.etName);
        etLastName = findViewById(R.id.etLastName);
        btnRegister = findViewById(R.id.btnRegister);

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
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(String.class, new EmptyStringSerializer()).create();
            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.113:5154/")
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();

            ProfileAPI profileAPI = retrofit.create(ProfileAPI.class);
            @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(etBirthdate.getText().toString()));
            Call<Void> call = profileAPI.createUser(
                    new NewUser(
                            new ProfileModel("",
                                    etFirstName.getText().toString(),
                                    etLastName.getText().toString(),
                                    "",
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
                    System.out.println();
                    Toast.makeText(RegisterActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex){
            Toast.makeText(this, "An Error Occurred", Toast.LENGTH_SHORT).show();
        }
    }
}