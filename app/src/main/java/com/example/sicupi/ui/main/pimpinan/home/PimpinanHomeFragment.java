package com.example.sicupi.ui.main.pimpinan.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sicupi.R;
import com.example.sicupi.data.api.ApiConfig;
import com.example.sicupi.data.api.PimpinanService;
import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.data.model.PimpinanModel;
import com.example.sicupi.data.model.ResponseModel;
import com.example.sicupi.data.model.UserModel;
import com.example.sicupi.databinding.FragmentPimpinanHomeBinding;
import com.example.sicupi.ui.main.pegawai.cuti.PegawaiHistoryCutiPentingFragment;
import com.example.sicupi.ui.main.pimpinan.adapter.AllPengajuanCutiAdapter;
import com.example.sicupi.ui.main.pimpinan.cuti.PimpinanHistoryCutiMelahirkanFragment;
import com.example.sicupi.ui.main.pimpinan.cuti.PimpinanHistoryCutiPentingFragment;
import com.example.sicupi.ui.main.pimpinan.cuti.PimpinanHistoryCutikurang14Fragment;
import com.example.sicupi.ui.main.pimpinan.cuti.PimpinanHistoryCutilebih14Fragment;
import com.example.sicupi.ui.main.pimpinan.profile.PimpinanProfileFragment;
import com.example.sicupi.util.Constants;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PimpinanHomeFragment extends Fragment {

    SharedPreferences sharedPreferences;
    List<CutiModel> cutiModelList;
    LinearLayoutManager linearLayoutManager;
    AllPengajuanCutiAdapter allPengajuanCutiAdapter;
    String userId;
    PimpinanService pimpinanService;
    AlertDialog progressDialog;

    private FragmentPimpinanHomeBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding  = FragmentPimpinanHomeBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHAREDPREFNAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constants.SHAREDPRE_USER_ID, null);
        pimpinanService = ApiConfig.getClient().create(PimpinanService.class);
        getMyProfile();




        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvUsername.setText(sharedPreferences.getString("nama", null));
        getAllCuti();
        listener();

    }


    private void listener() {

        binding.btnCutiMelahirkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new PimpinanHistoryCutiMelahirkanFragment());
            }
        });

        binding.btnCutiKurang14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new PimpinanHistoryCutikurang14Fragment());
            }
        });

        binding.btnCutiLebih14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new PimpinanHistoryCutilebih14Fragment());
            }
        });

        binding.btnCutiPenting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new PimpinanHistoryCutiPentingFragment());
            }
        });


        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new PimpinanProfileFragment());
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new PimpinanProfileFragment());
            }
        });
    }



    private void moveFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framePimpinan, fragment)
                .addToBackStack(null).commit();
    }

    private void getAllCuti() {
        showProgressBar("Loading", "Memuat data cuti...", true);
        pimpinanService.getAllPengajuanCuti().enqueue(new Callback<List<CutiModel>>() {
            @Override
            public void onResponse(Call<List<CutiModel>> call, Response<List<CutiModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    showProgressBar("adad", "asdad", false);
                    cutiModelList = response.body();
                    allPengajuanCutiAdapter = new AllPengajuanCutiAdapter(getContext(), cutiModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvCuti.setLayoutManager(linearLayoutManager);
                    binding.rvCuti.setAdapter(allPengajuanCutiAdapter);
                    binding.rvCuti.setHasFixedSize(true);
                    binding.tvEmpty.setVisibility(View.GONE);


                }else {
                    showProgressBar("sds", "dsds", false);
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<CutiModel>> call, Throwable t) {
                showProgressBar("sds", "dsds", false);
                showToast("error", "Tidak ada koneksi internet");
                binding.tvEmpty.setVisibility(View.GONE);

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



    private void getMyProfile() {
        showProgressBar("Loading", "Memuat data...", true);
        pimpinanService.getMyProfile(userId).enqueue(new Callback<PimpinanModel>() {
            @Override
            public void onResponse(Call<PimpinanModel> call, Response<PimpinanModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showProgressBar("sds", "adad", false);
                    Glide.with(getContext())
                            .load(response.body().getFoto())
                            .centerCrop()
                            .fitCenter()
                            .centerInside()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(binding.profileImage);



                }else {
                    showProgressBar("sds", "adad", false);
                    showToast("error", "Gagal memuat data pegawai");
                }
            }

            @Override
            public void onFailure(Call<PimpinanModel> call, Throwable t) {

                showProgressBar("sds", "adad", false);
                showToast("error", "Tidak ada koneksi internet");


            }
        });

    }




}