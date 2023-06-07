package com.example.sicupi.data.api;

import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.data.model.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PimpinanService {

    @GET("pimpinan/getAllPengajuanCuti")
    Call<List<CutiModel>> getAllPengajuanCuti();

    @GET("pimpinan/getAllPengajuanCutiByKeterangan")
    Call<List<CutiModel>> getAllPengajuanCutiByKeterangan(
            @Query("keterangan") String keterangan
    );

    @FormUrlEncoded
    @POST("pimpinan/tolakCuti")
    Call<ResponseModel> tolakCuti(
            @Field("user_id") String userId,
            @Field("cuti_id") String cutiId
    );

    @FormUrlEncoded
    @POST("pimpinan/setujuCuti")
    Call<ResponseModel> setujuCuti(
            @Field("user_id") String userId,
            @Field("cuti_id") String cutiId
    );


}
