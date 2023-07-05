package com.example.sicupi.data.api;

import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.data.model.ResponseModel;
import com.example.sicupi.data.model.UserModel;

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

    @GET("pegawai/checkCutiAktif")
    Call<CutiModel> checkCutiAktif(
            @Query("id") String id
    );

    @FormUrlEncoded
    @POST("pegawai/konfirmasiCutiSelesai")
    Call<ResponseModel> konfirmasiCutiSelesai(
            @Field("id") String idCuti,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("pegawai/dismissNotif")
    Call<ResponseModel> dismissNotif(
            @Field("user_id") String userId
    );

    @Multipart
    @POST("pegawai/editProfile")
    Call<ResponseModel> editProfile(

            @PartMap Map<String, RequestBody> textData
    );

    @GET("pegawai/getTotalCuti")
    Call<List<CutiModel>> totalCutiModelProfile(
            @Query("user_id") String userId,
            @Query("verifikasi") Integer verifikasi
    );

    @Multipart
    @POST("pegawai/editPhotoProfile")
    Call<ResponseModel> updatePhotoProfile(
            @PartMap Map<String, RequestBody> textData,
            @Part MultipartBody.Part file
    );

    @GET("pegawai/checkTotalCuti")
    Call<ResponseModel> verifiedTotalCuti(
            @Query("kode_pegawai") String kodePegawai
    );

    @GET("pegawai/checkTotalCutiMelahirkan")
    Call<ResponseModel> verifiedTotalCutiMelahirkan(
            @Query("kode_pegawai") String kodePegawai
    );














}
