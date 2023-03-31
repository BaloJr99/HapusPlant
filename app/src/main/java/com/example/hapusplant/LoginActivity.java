package com.example.hapusplant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hapusplant.database.HapusPlantLiteDb;
import com.example.hapusplant.interfaces.UserAPI;
import com.example.hapusplant.models.UserModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    HapusPlantLiteDb hapusDb;
    Button btnLogin;
    EditText etUsername, etPassword;
    TextView tvRegister, tvForgot, tvUsernameValidation, tvPasswordValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        hapusDb = new HapusPlantLiteDb(this);

        if(!hapusDb.getJwtIfExists().isEmpty()){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgot = findViewById(R.id.tvForgot);
        tvRegister = findViewById(R.id.tvRegister);
        tvUsernameValidation = findViewById(R.id.tvUsernameValidation);
        tvPasswordValidation = findViewById(R.id.tvPasswordValidation);

        btnLogin.setOnClickListener(view -> {
            if(!emptyFields()){
                loginUser();
            }
        });

        tvForgot.setOnClickListener(view -> {
        });

        tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private boolean emptyFields(){
        boolean flag = false;
        if(etUsername.getText().toString().isEmpty()){
            tvUsernameValidation.setVisibility(View.VISIBLE);
            flag = true;
        }else{
            tvUsernameValidation.setVisibility(View.GONE);
        }
        if(etPassword.getText().toString().isEmpty()){
            tvPasswordValidation.setVisibility(View.VISIBLE);
            flag = true;
        }else{
            tvPasswordValidation.setVisibility(View.GONE);
        }
        return flag;
    }

    private void loginUser(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.113:5154/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        UserAPI userAPI = retrofit.create(UserAPI.class);
        Call<Void> call = userAPI.login(new UserModel(etUsername.getText().toString(), etPassword.getText().toString()));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                try {
                    if(response.isSuccessful()){
                        try {
                            List<String> cookies = response.headers().values("Set-Cookie");
                            Map<String, String> dividedCookies = new HashMap<>();
                            for (String cookie: cookies) {
                                String[] cookieToSplit = cookie.split("=", 2);
                                dividedCookies.put(cookieToSplit[0], cookieToSplit[1]);
                            }

                            String token = dividedCookies.get("X-Access-Token");

                            if(token == null){
                                Toast.makeText(LoginActivity.this, "Authentication Error, Please Wait a Moment", Toast.LENGTH_SHORT).show();
                            }else {
                                hapusDb.insertJwt(token);
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }catch (Exception ex){
                            Toast.makeText(LoginActivity.this, "Authentication Error, Please Wait a Moment", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "Incorrect Username/Password", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex){
                    Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}