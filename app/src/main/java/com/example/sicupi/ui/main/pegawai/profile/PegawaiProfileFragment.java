package com.example.sicupi.ui.main.pegawai.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sicupi.R;
import com.example.sicupi.data.api.ApiConfig;
import com.example.sicupi.data.api.PegawaiService;
import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.data.model.UserModel;
import com.example.sicupi.databinding.FragmentPegawaiProfileBinding;
import com.example.sicupi.ui.main.auth.LoginActivity;
import com.example.sicupi.util.Constants;

import org.w3c.dom.Text;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PegawaiProfileFragment extends Fragment {
    private FragmentPegawaiProfileBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    PegawaiService pegawaiService;
    AlertDialog progressDialog;
    String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPegawaiProfileBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHAREDPREFNAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constants.SHAREDPRE_USER_ID, null);
        editor = sharedPreferences.edit();
        pegawaiService = ApiConfig.getClient().create(PegawaiService.class);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTotalCuti(1, binding.tvSetuju);
        getTotalCuti(2, binding.tvTolak);
        getMyProfile();
        listener();


    }

    private void listener() {
        binding.cvUbahProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framePegawai, new PegawaiEditProfileFragment())
                        .addToBackStack(null).commit();
            }
        });
        binding.cvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
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
                    binding.tvNama.setText(response.body().getNama());
                    binding.tvJabatan.setText(response.body().getJabatan());


                }else {
                    showProgressBar("sds", "adad", false);
                    showToast("error", "Gagal memuat data pegawai");

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

                showProgressBar("sds", "adad", false);
                showToast("error", "Tidak ada koneksi internet");



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

    private void getTotalCuti(Integer verifikasi, TextView tvTotal) {
        showProgressBar("Loading", "Memuat data...", true);
        pegawaiService.totalCutiModelProfile(userId, verifikasi).enqueue(new Callback<List<CutiModel>>() {
            @Override
            public void onResponse(Call<List<CutiModel>> call, Response<List<CutiModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    tvTotal.setText(String.valueOf(response.body().size()));
                    showProgressBar("dsdsd", "sdsds", false);
                }else {
                    tvTotal.setText("0");
                    showProgressBar("dsdsd", "sdsds", false);
                }
            }

            @Override
            public void onFailure(Call<List<CutiModel>> call, Throwable t) {
                tvTotal.setText("-");
                showProgressBar("dsdsd", "sdsds", false);
                showToast("error", "Tidak ada koneksi internet");

            }
        });
    }

    private void logOut() {
        editor.clear().apply();

    }
}