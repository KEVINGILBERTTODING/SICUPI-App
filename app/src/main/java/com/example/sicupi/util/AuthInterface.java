package com.example.sicupi.util;

import com.example.sicupi.model.AuthModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface AuthInterface {

    @FormUrlEncoded
    @POST("authapi/login")
    Call<AuthModel>login(
            @Field("email") String email,
            @Field("password") String password
    );
}
