package com.example.sicupi.data.api;

import com.example.sicupi.data.model.CutiModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PimpinanService {

    @GET("pimpinan/getAllPengajuanCuti")
    Call<List<CutiModel>> getAllPengajuanCuti();

    @GET("pimpinan/getAllPengajuanCutiByKeterangan")
    Call<List<CutiModel>> getAllPengajuanCutiByKeterangan(
            @Query("keterangan") String keterangan
    );
}
