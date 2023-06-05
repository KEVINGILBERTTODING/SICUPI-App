package com.example.sicupi.data.api;

import com.example.sicupi.data.model.CutiModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PegawaiService {
    @GET("pegawai/getCutiByUserId")
    Call<List<CutiModel>> getCutiByUserId(
            @Query("user_id") String userId,
            @Query("keterangan") String keterangan
    );

    @GET("pegawai/getAllCutiByUserId")
    Call<List<CutiModel>> getAllCutiByUserId(
            @Query("user_id") String userId
    );


}
