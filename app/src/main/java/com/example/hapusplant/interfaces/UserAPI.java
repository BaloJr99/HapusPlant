package com.example.hapusplant.interfaces;

import com.example.hapusplant.models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserAPI {
    @POST("Authentication/LoginUser")
    Call<UserModel> login(@Body UserModel userModel);
}
