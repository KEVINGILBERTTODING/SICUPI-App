package com.example.sicupi.data.api;

import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.data.model.ResponseModel;
import com.example.sicupi.data.model.UserModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    @GET("pegawai/getShowCuti")
    Call<CutiModel> getShowCuti(
            @Query("user_id") String userId
    );

    @Multipart
    @POST("pegawai/insertCutiMelahirkan")
    Call<ResponseModel> inserCutiMelahirkan(
            @PartMap Map<String, RequestBody> textData,
            @Part MultipartBody.Part file
            );

    @GET("pegawai/getMyProfile")
    Call<UserModel> getMyProfile(
            @Query("id") String id
    ) ;

    @Multipart
    @POST("pegawai/insertCutiKurang14")
    Call<ResponseModel> insertCutiKurang14(
            @PartMap Map<String, RequestBody> textData,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("pegawai/insertCutiLebih14")
    Call<ResponseModel> insertCutiLebih14(
            @PartMap Map<String, RequestBody> textData,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("pegawai/insertCutiPenting")
    Call<ResponseModel> insertCutiPenting(
            @PartMap Map<String, RequestBody> textData,
            @Part MultipartBody.Part file
    );











}
