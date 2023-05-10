package com.example.sicupi.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataApi {

    public static final String IP_ADDRESS ="192.168.41.4";// ip address
    public static final String BASE_URL ="http://" + IP_ADDRESS+"/pegawai/api/";

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
