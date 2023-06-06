package com.example.sicupi.ui.main.pegawai.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sicupi.data.api.ApiConfig;
import com.example.sicupi.data.api.PegawaiService;
import com.example.sicupi.data.model.ResponseModel;
import com.example.sicupi.data.model.UserModel;
import com.example.sicupi.databinding.FragmentPegawaiEditProfileBinding;
import com.example.sicupi.util.Constants;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PegawaiEditProfileFragment extends Fragment {
    SharedPreferences sharedPreferences;
    String userId;
    PegawaiService pegawaiService;
    AlertDialog progressDialog;
    private FragmentPegawaiEditProfileBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPegawaiEditProfileBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHAREDPREFNAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constants.SHAREDPRE_USER_ID, null);
        pegawaiService = ApiConfig.getClient().create(PegawaiService.class);
        getMyProfile();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       listener();

    }
    private void listener() {

        binding.btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        binding.btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etNik.getText().toString().isEmpty()){
                    binding.etNik.setError("Kolom tidak boleh kosong");
                    binding.etNik.requestFocus();
                }else if (binding.etNama.getText().toString().isEmpty()){
                    binding.etNama.setError("Kolom tidak boleh kosong");
                    binding.etNama.requestFocus();
                }else if (binding.etAlamat.getText().toString().isEmpty()){
                    binding.etAlamat.setError("Kolom tidak boleh kosong");
                    binding.etAlamat.requestFocus();
                }else if (binding.etTelepn.getText().toString().isEmpty()){
                    binding.etTelepn.setError("Kolom tidak boleh kosong");
                    binding.etTelepn.requestFocus();
                }
                else if (binding.etEmail.getText().toString().isEmpty()){
                    binding.etEmail.setError("Kolom tidak boleh kosong");
                    binding.etEmail.requestFocus();
                }
                else if (binding.etPassword.getText().toString().isEmpty()){
                    binding.etEmail.setError("Kolom tidak boleh kosong");
                    binding.etEmail.requestFocus();
                }else {
                    simpanData();
                }
            }
        });





    }

    private void getMyProfile() {
        showProgressBar("Loading", "Memuat data...", true);
        pegawaiService.getMyProfile(userId).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showProgressBar("sds", "adad", false);
                    binding.etNik.setText(response.body().getNik());
                    binding.etNama.setText(response.body().getNama());
                    binding.etAlamat.setText(response.body().getAlamat());
                    binding.etTelepn.setText(response.body().getNomorTelepon());
                    binding.etEmail.setText(response.body().getEmail());
                    binding.etPassword.setText(response.body().getPassword());


                }else {
                    showProgressBar("sds", "adad", false);
                    showToast("error", "Gagal memuat data pegawai");
                    binding.btnSimpan.setEnabled(false);

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

                showProgressBar("sds", "adad", false);
                showToast("error", "Tidak ada koneksi internet");
                binding.btnSimpan.setEnabled(false);



            }
        });

    }

    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setCancelable(false);
                progressDialog = builder.create();
            }
            progressDialog.show(); // Menampilkan progress dialog
        } else {
            // Menyembunyikan progress dialog jika ada
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
    private void showToast(String jenis, String text) {
        if (jenis.equals("success")) {
            Toasty.success(getContext(), text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(getContext(), text, Toasty.LENGTH_SHORT).show();
        }
    }

    private void simpanData() {
        showProgressBar("Loading", "Menyimpan perubahan...", true);
        HashMap map = new HashMap();
        map.put("nik", RequestBody.create(MediaType.parse("text/plain"), binding.etNik.getText().toString()));
        map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), userId));
        map.put("nama", RequestBody.create(MediaType.parse("text/plain"), binding.etNama.getText().toString()));
        map.put("alamat", RequestBody.create(MediaType.parse("text/plain"), binding.etAlamat.getText().toString()));
        map.put("no_telp", RequestBody.create(MediaType.parse("text/plain"), binding.etTelepn.getText().toString()));
        map.put("email", RequestBody.create(MediaType.parse("text/plain"), binding.etEmail.getText().toString()));
        map.put("password", RequestBody.create(MediaType.parse("text/plain"), binding.etPassword.getText().toString()));
        pegawaiService.editProfile(map).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call <ResponseModel>call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body().getStatus() == 200) {
                    showToast("success", "Berhasil mengubah profil");
                    showProgressBar("dsd", "sdds", false);
                    getActivity().onBackPressed();
                }else {
                    showToast("error", response.body().getMessage());
                    showProgressBar("dsd", "Sd", false);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showToast("error", "Tidak ada koneksi internet");
                showProgressBar("dsd", "SDdsd", false);
            }
        });


    }

}