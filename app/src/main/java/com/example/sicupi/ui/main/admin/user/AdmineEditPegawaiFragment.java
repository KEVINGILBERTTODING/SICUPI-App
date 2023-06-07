package com.example.sicupi.ui.main.admin.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.sicupi.databinding.FragmentEditUserBinding;
import com.example.sicupi.databinding.FragmentInsertUserBinding;
import com.example.sicupi.util.Constants;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdmineEditPegawaiFragment extends Fragment {

    String userId;
    AdminService adminService;
    AlertDialog progressDialog;
    PegawaiService pegawaiService;
    String jenisKelamin, jabatan, status;
    private FragmentEditUserBinding binding;
    String [] opsiJk = {"Laki-laki", "Perempuan"};
    String [] opsiStatus = {"Aktif", "Keluar", "Tidak Aktif"};
    String [] opsiJabatan = {
        "Staf Humas", "Staff Keuangan", "Staff Rumah Tangga", "Staff Umum", "Staff Persidangan", "Staff Perlengkapan"
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditUserBinding.inflate(inflater, container, false);
        adminService = ApiConfig.getClient().create(AdminService.class);
        pegawaiService = ApiConfig.getClient().create(PegawaiService.class);

        ArrayAdapter adapterJk = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, opsiJk);
        adapterJk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spJk.setAdapter(adapterJk);

        ArrayAdapter adapterJabatan = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, opsiJabatan);
        adapterJabatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spJabatan.setAdapter(adapterJabatan);

        ArrayAdapter adapterStatus = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, opsiStatus);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spStatus.setAdapter(adapterStatus);




        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userId = getArguments().getString("user_id");
        getPegawaiProfile();

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
                else {
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

        binding.spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = opsiStatus[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Peringatan").setMessage("Apakah anda yakin?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteUser();
                            }
                        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                alert.show();
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
        map.put("status", RequestBody.create(MediaType.parse("text/plain"), status));
        map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), userId));

        map.put("email", RequestBody.create(MediaType.parse("text/plain"), binding.etEmail.getText().toString()));
        adminService.editPegawai(map).enqueue(new Callback<ResponseModel>() {
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

    private void getPegawaiProfile() {
        showProgressBar("Loading", "Memuat data...", true);
        pegawaiService.getMyProfile(userId).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showProgressBar("sds", "adad", false);
                    binding.etNIK.setText(response.body().getNik());
                    binding.etNama.setText(response.body().getNama());
                    binding.etAlamat.setText(response.body().getAlamat());
                    binding.etTelepn.setText(response.body().getNomorTelepon());
                    binding.etEmail.setText(response.body().getEmail());
                    binding.spJk.setSelection(response.body().getJenisKelamin().equals("Laki-laki") ? 0 : 1);


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

    private void deleteUser() {
        showProgressBar("Loading", "Menghapus Pegawai...", true);
        adminService.deletePegawai(userId).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body().getStatus() == 200) {
                    showProgressBar("dsds", "Dsd", false);
                    showToast("success", "Berhasil menghapus pegawai");
                    getActivity().onBackPressed();
                }else {
                    showProgressBar("dsds", "Dsd", false);
                    showToast("error", "Gagal menghapus pegawai");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("dsds", "Dsd", false);
                showToast("error", "Tidak ada koneksi internet");

            }
        });
    }

}