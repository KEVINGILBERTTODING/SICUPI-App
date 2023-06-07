package com.example.sicupi.ui.main.pimpinan.cuti;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sicupi.R;
import com.example.sicupi.data.api.ApiConfig;
import com.example.sicupi.data.api.PimpinanService;
import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.databinding.FragmentPimpinanHistoryCutiKurang14Binding;
import com.example.sicupi.ui.main.pimpinan.adapter.HistoryAllCutiAdapter;
import com.example.sicupi.util.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PimpinanHistoryCutikurang14Fragment extends Fragment {

    private FragmentPimpinanHistoryCutiKurang14Binding binding;
    SharedPreferences sharedPreferences;
    private AlertDialog progressDialog;
    String userId;
    HistoryAllCutiAdapter historyAllCutiAdapter;
    List<CutiModel> cutiModelList;
    LinearLayoutManager linearLayoutManager;
    PimpinanService pimpinanService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPimpinanHistoryCutiKurang14Binding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHAREDPREFNAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constants.SHAREDPRE_USER_ID, null);
        pimpinanService = ApiConfig.getClient().create(PimpinanService.class);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData("Cuti Sakit < 14");
        listener();




    }
    private void getData(String keterangan) {
        showProgressBar("Loading", "Memuat data...", true);
        pimpinanService.getAllPengajuanCutiByKeterangan(keterangan).enqueue(new Callback<List<CutiModel>>() {
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