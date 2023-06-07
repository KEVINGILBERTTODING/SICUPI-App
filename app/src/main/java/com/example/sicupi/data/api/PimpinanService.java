package com.example.sicupi.data.api;

import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.data.model.PimpinanModel;
import com.example.sicupi.data.model.ResponseModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    @GET("pimpinan/getMyProfile")
    Call<PimpinanModel> getMyProfile(
            @Query("user_id") String userId
    );

    @Multipart
    @POST("pimpinan/editPhotoProfile")
    Call<ResponseModel> updatePhotoProfile(
            @PartMap Map<String, RequestBody> textData,
            @Part MultipartBody.Part image
            );


    @Multipart
    @POST("pimpinan/editProfile")
    Call<ResponseModel> editProfile(
            @PartMap Map<String, RequestBody> textData
    );





}
