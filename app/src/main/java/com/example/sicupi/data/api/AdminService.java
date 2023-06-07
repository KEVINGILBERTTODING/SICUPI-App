package com.example.sicupi.data.api;

import com.example.sicupi.data.model.AdminModel;
import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.data.model.ResponseModel;
import com.example.sicupi.data.model.UserModel;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface AdminService {

    @GET("admin/getMyProfile")
    Call<AdminModel> getMyProfile(
            @Query("user_id") String userId
    );

    @GET("admin/getalluser")
    Call<List<UserModel>> getAllUsers();
    @GET("admin/getAllUserTidakPernahCuti")
    Call<List<UserModel>> getAllUsersTidakPernahCuti();

    @GET("admin/getAllTotalPengajuanCuti")
    Call<List<CutiModel>> getAllTotalCuti(
            @Query("keterangan") String keterangan
    );

    @Multipart
    @POST("admin/insertPegawai")
    Call<ResponseModel> insertPegawai(
            @PartMap Map<String, RequestBody> text
            );


}
