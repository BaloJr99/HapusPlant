package com.example.hapusplant.interfaces;

import com.example.hapusplant.models.NewUser;
import com.example.hapusplant.models.SearchSucculentType;
import com.example.hapusplant.models.SucculentFamily;
import com.example.hapusplant.models.SucculentType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SucculentKindAPI {

    @GET("SucculentKind")
    Call<List<SearchSucculentType>> getAllSucculents(@Header("Cookie") String token);

    @POST("SucculentKind/CreateSucculentKind")
    Call<Void> createSucculent(@Body SucculentType succulentType, @Header("Cookie") String token);

    @GET("SucculentKind/{kindId}")
    Call<SucculentType> getSucculentKindById(@Path("kindId") String kindId, @Header("Cookie") String token);

    @PUT("SucculentKind/EditSucculentKind")
    Call<Void> editSucculent(@Body SucculentType succulentType, @Header("Cookie") String token);
}
