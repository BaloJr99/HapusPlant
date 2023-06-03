package com.example.hapusplant.interfaces;

import com.example.hapusplant.models.NewUser;
import com.example.hapusplant.models.SharedCollectionContacts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SharedContactsAPI {
    @GET("SharedCollection")
    Call<List<SharedCollectionContacts>> getSharedContacts(@Header("Cookie") String token);

    @DELETE("SharedCollection/DeleteSharedCollection/{idSharedUser}")
    Call<Void> deleteSharedContact(@Path("idSharedUser") String idSharedUser, @Header("Cookie") String token);

    @POST("SharedCollection/AddSharedCollection/{idSharedUser}")
    Call<Void> addNewSharedUser(@Path("idSharedUser") String idUser, @Header("Cookie") String token);
}
