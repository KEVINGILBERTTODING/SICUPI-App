package com.example.sicupi.ui.main.admin.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sicupi.data.api.AdminService;
import com.example.sicupi.data.api.ApiConfig;
import com.example.sicupi.data.api.PegawaiService;
import com.example.sicupi.data.model.ResponseModel;
import com.example.sicupi.data.model.UserModel;
import com.example.sicupi.databinding.FragmentInsertUserBinding;
import com.example.sicupi.databinding.FragmentPegawaiEditProfileBinding;
import com.example.sicupi.util.Constants;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminInsertPegawaiFragment extends Fragment {
    SharedPreferences sharedPreferences;
    String userId;
    AdminService adminService;
    AlertDialog progressDialog;
    String jenisKelamin, jabatan;
    private FragmentInsertUserBinding binding;
    String [] opsiJk = {"Laki-laki", "Perempuan"};
    String [] opsiJabatan = {
        "Staf Humas", "Staff Keuangan", "Staff Rumah Tangga", "Staff Umum", "Staff Persidangan", "Staff Perlengkapan"
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInsertUserBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHAREDPREFNAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constants.SHAREDPRE_USER_ID, null);
        adminService = ApiConfig.getClient().create(AdminService.class);

        ArrayAdapter adapterJk = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, opsiJk);
        adapterJk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spJk.setAdapter(adapterJk);

        ArrayAdapter adapterJabatan = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, opsiJabatan);
        adapterJabatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spJabatan.setAdapter(adapterJabatan);


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
        binding.spJk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jenisKelamin = opsiJk[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spJabatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jabatan = opsiJabatan[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etNIK.getText().toString().isEmpty()){
                    binding.etNIK.setError("Kolom tidak boleh kosong");
                    binding.etNIK.requestFocus();
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
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
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
        map.put("nik", RequestBody.create(MediaType.parse("text/plain"), binding.etNIK.getText().toString()));
        map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), userId));
        map.put("jabatan", RequestBody.create(MediaType.parse("text/plain"), jabatan));
        map.put("jenis_kel", RequestBody.create(MediaType.parse("text/plain"), jenisKelamin));
        map.put("nama", RequestBody.create(MediaType.parse("text/plain"), binding.etNama.getText().toString()));
        map.put("alamat", RequestBody.create(MediaType.parse("text/plain"), binding.etAlamat.getText().toString()));
        map.put("notelp", RequestBody.create(MediaType.parse("text/plain"), binding.etTelepn.getText().toString()));
        map.put("email", RequestBody.create(MediaType.parse("text/plain"), binding.etEmail.getText().toString()));
        map.put("password", RequestBody.create(MediaType.parse("text/plain"), binding.etPassword.getText().toString()));
        adminService.insertPegawai(map).enqueue(new Callback<ResponseModel>() {
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