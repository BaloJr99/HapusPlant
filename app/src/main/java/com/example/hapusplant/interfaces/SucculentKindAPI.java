package com.example.hapusplant.interfaces;

import com.example.hapusplant.models.NewUser;
import com.example.hapusplant.models.SucculentFamily;
import com.example.hapusplant.models.SucculentType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SucculentKindAPI {

    @GET("SucculentKind")
    Call<List<SucculentType>> getAllSucculents(@Header("Cookie") String token);

    @POST("SucculentKind/CreateSucculentKind")
    Call<Void> createSucculent(@Body SucculentType succulentType, @Header("Cookie") String token);
}
