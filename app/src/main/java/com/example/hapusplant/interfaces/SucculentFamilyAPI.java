package com.example.hapusplant.interfaces;

import com.example.hapusplant.models.SucculentFamily;
import com.example.hapusplant.models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface SucculentFamilyAPI {
    @GET("SucculentFamily")
    Call<List<SucculentFamily>> getFamilies(@Header("Cookie") String token);
}
