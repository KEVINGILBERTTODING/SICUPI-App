package com.example.sicupi.data.api;

import com.example.sicupi.util.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConfig {


    public static final String BASE_URL ="http://" + Constants.IP_ADDRESS +"/pegawai/api/";

    public static Retrofit retrofit = null;
    public static Retrofit getClient(){
        if (retrofit == null) {
            retrofit  = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
