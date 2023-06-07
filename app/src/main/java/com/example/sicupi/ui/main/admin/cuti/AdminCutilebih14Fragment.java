package com.example.sicupi.ui.main.admin.cuti;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sicupi.data.api.AdminService;
import com.example.sicupi.data.api.ApiConfig;
import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.databinding.FragmentAdminCutiKurang14Binding;
import com.example.sicupi.databinding.FragmentAdminCutiLebih14Binding;
import com.example.sicupi.ui.main.pimpinan.adapter.HistoryAllCutiAdapter;
import com.example.sicupi.util.Constants;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCutilebih14Fragment extends Fragment {

    private FragmentAdminCutiLebih14Binding binding;
    SharedPreferences sharedPreferences;
    private AlertDialog progressDialog;
    String userId;
    AdminService adminService;
    HistoryAllCutiAdapter historyAllCutiAdapter;
    List<CutiModel> cutiModelList;
    LinearLayoutManager linearLayoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminCutiLebih14Binding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHAREDPREFNAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constants.SHAREDPRE_USER_ID, null);
        adminService = ApiConfig.getClient().create(AdminService.class);
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Semua"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Selesai"));


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllCuti("Cuti Sakit > 14");
        listener();




    }
    private void getAllCuti(String keterangan) {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAllPengajuanCutiAdminKeterangan(keterangan).enqueue(new Callback<List<CutiModel>>() {
            @Override
            public void onResponse(Call<List<CutiModel>> call, Response<List<CutiModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    showProgressBar("Loading", "Memuat data...", false);
                    cutiModelList = response.body();
                    historyAllCutiAdapter = new HistoryAllCutiAdapter(getContext(), cutiModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvCuti.setLayoutManager(linearLayoutManager);
                    binding.rvCuti.setAdapter(historyAllCutiAdapter);
                    binding.rvCuti.setHasFixedSize(true);
                    binding.tvEmpty.setVisibility(View.GONE);


                }else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    showProgressBar("Loading", "Memuat data...", false);


                }
            }

            @Override
            public void onFailure(Call<List<CutiModel>> call, Throwable t) {
                binding.tvEmpty.setVisibility(View.GONE);
                showProgressBar("Loading", "Memuat data...", false);
                showToast("gagal", "Tidak ada koneksi internet");
            }
        });

    }
    private void getAllCutiSelesai(String keterangan) {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAllPengajuanCutiAdminSelesai(keterangan).enqueue(new Callback<List<CutiModel>>() {
            @Override
            public void onResponse(Call<List<CutiModel>> call, Response<List<CutiModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    showProgressBar("Loading", "Memuat data...", false);
                    cutiModelList = response.body();
                    historyAllCutiAdapter = new HistoryAllCutiAdapter(getContext(), cutiModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvCuti.setLayoutManager(linearLayoutManager);
                    binding.rvCuti.setAdapter(historyAllCutiAdapter);
                    binding.rvCuti.setHasFixedSize(true);
                    binding.tvEmpty.setVisibility(View.GONE);


                }else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    showProgressBar("Loading", "Memuat data...", false);


                }
            }

            @Override
            public void onFailure(Call<List<CutiModel>> call, Throwable t) {
                binding.tvEmpty.setVisibility(View.GONE);
                showProgressBar("Loading", "Memuat data...", false);
                showToast("gagal", "Tidak ada koneksi internet");
            }
        });

    }

    private void listener() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    binding.rvCuti.setAdapter(null);

                    getAllCuti("Cuti Sakit > 14");
                }else if (tab.getPosition() == 1) {
                    binding.rvCuti.setAdapter(null);

                    getAllCutiSelesai("Cuti Sakit > 14");

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

   private void filter(String keyWord) {
       ArrayList<CutiModel> filteredList = new ArrayList<>();
       for (CutiModel item : cutiModelList) {
           if (item.getNama().toLowerCase().contains(keyWord.toLowerCase())) {
               filteredList.add(item);
           }
       }
       historyAllCutiAdapter.filter(filteredList);
       if (filteredList.isEmpty()) {

       }else {
           historyAllCutiAdapter.filter(filteredList);
       }


   }

}