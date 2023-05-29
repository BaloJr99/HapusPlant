package com.example.hapusplant.interfaces;

import com.example.hapusplant.models.NewUser;
import com.example.hapusplant.models.SharedCollectionContacts;
import com.example.hapusplant.models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProfileAPI {
    @POST("User/CreateNewUser")
    Call<Void> createUser(@Body NewUser newUser);

    @GET("Profile/GetMatchingUsers/{username}")
    Call<List<SharedCollectionContacts>> getMatchingUsername(@Path("username") String username, @Header("Cookie") String token);
}
