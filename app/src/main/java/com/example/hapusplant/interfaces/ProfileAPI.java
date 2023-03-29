package com.example.hapusplant.interfaces;

import com.example.hapusplant.models.NewUser;
import com.example.hapusplant.models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProfileAPI {
    @POST("User/CreateNewUser")
    Call<Void> createUser(@Body NewUser newUser);
}
