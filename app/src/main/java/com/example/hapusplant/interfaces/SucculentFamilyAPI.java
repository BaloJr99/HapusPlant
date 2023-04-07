package com.example.hapusplant.interfaces;

import com.example.hapusplant.models.NewUser;
import com.example.hapusplant.models.SucculentFamily;
import com.example.hapusplant.models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SucculentFamilyAPI {
    @GET("SucculentFamily")
    Call<List<SucculentFamily>> getFamilies(@Header("Cookie") String token);

    @POST("SucculentFamily/CreateSucculentFamily")
    Call<Void> createFamily(@Body SucculentFamily family, @Header("Cookie") String token);
}
