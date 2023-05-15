package com.example.hapusplant.interfaces;

import com.example.hapusplant.models.NewUser;
import com.example.hapusplant.models.SharedCollectionContacts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SharedContactsAPI {
    @GET("SharedCollection")
    Call<List<SharedCollectionContacts>> getSharedContacts(@Header("Cookie") String token);
}
