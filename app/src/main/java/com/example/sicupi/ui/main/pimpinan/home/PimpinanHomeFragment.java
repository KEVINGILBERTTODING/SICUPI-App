package com.example.sicupi.ui.main.pimpinan.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sicupi.R;
import com.example.sicupi.data.api.ApiConfig;
import com.example.sicupi.data.api.PegawaiService;
import com.example.sicupi.data.api.PimpinanService;
import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.data.model.ResponseModel;
import com.example.sicupi.data.model.UserModel;
import com.example.sicupi.databinding.FragmentPegawaiHomeBinding;
import com.example.sicupi.databinding.FragmentPimpinanHomeBinding;
import com.example.sicupi.ui.main.pimpinan.adapter.AllPengajuanCutiAdapter;
import com.example.sicupi.ui.main.pimpinan.adapter.HistoryAllCutiAdapter;
import com.example.sicupi.ui.main.pimpinan.cuti.PegawaiHistoryCutiLebih14Fragment;
import com.example.sicupi.ui.main.pimpinan.cuti.PegawaiHistoryCutiMelahirkanFragment;
import com.example.sicupi.ui.main.pimpinan.cuti.PegawaiHistoryCutiPentingFragment;
import com.example.sicupi.ui.main.pimpinan.cuti.PegawaiHistoryCutikurang14Fragment;
import com.example.sicupi.ui.main.pimpinan.profile.PegawaiProfileFragment;
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
                moveFragment(new PegawaiHistoryCutiMelahirkanFragment());
            }
        });

        binding.btnCutiKurang14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new PegawaiHistoryCutikurang14Fragment());
            }
        });

        binding.btnCutiLebih14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new PegawaiHistoryCutiLebih14Fragment());
            }
        });

        binding.btnCutiPenting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new PegawaiHistoryCutiPentingFragment());
            }
        });


        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new PegawaiProfileFragment());
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



//    private void getMyProfile() {
//        showProgressBar("Loading", "Memuat data...", true);
//        pegawaiService.getMyProfile(userId).enqueue(new Callback<UserModel>() {
//            @Override
//            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    showProgressBar("sds", "adad", false);
//                    Glide.with(getContext())
//                            .load(response.body().getFoto())
//                            .centerCrop()
//                            .fitCenter()
//                            .centerInside()
//                            .skipMemoryCache(true)
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .into(binding.profileImage);
//
//                    Log.d("foto profile", "onResponse: " + response.body().getFoto());
//
//
//                    // jika ada pemberitahuan cuti disetujui
//                    // atau cuti ditolak
//                    if (response.body().getStatusPengajuan().equals("1")){ // jika cuti di setujui
//                        Dialog dialog = new Dialog(getContext());
//                        dialog.setContentView(R.layout.layout_alert_disetujui);
//                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        Button btnOke = dialog.findViewById(R.id.btnOke);
//                        dialog.show();
//                        btnOke.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                pegawaiService.dismissNotif(userId).enqueue(new Callback<ResponseModel>() {
//                                    @Override
//                                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
//                                        if (response.isSuccessful() && response.body().getStatus() == 200) {
//                                            dialog.dismiss();
//                                        }else {
//                                            showToast("error", "Terjadi kesalahan");
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ResponseModel> call, Throwable t) {
//                                        showToast("error", "Tidak ada koneksi internet");
//                                    }
//                                });
//                                dialog.dismiss();
//                            }
//                        });
//                    }else if (response.body().getStatusPengajuan().equals("2")){
//                        Dialog dialog = new Dialog(getContext());
//                        dialog.setContentView(R.layout.layout_alert_ditolak);
//                        dialog.setCancelable(false);
//                        dialog.setCanceledOnTouchOutside(false);
//                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        Button btnOke = dialog.findViewById(R.id.btnOke);
//                        dialog.show();
//
//                        btnOke.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                pegawaiService.dismissNotif(userId).enqueue(new Callback<ResponseModel>() {
//                                    @Override
//                                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
//                                        if (response.isSuccessful() && response.body().getStatus() == 200) {
//                                            dialog.dismiss();
//                                        }else {
//                                            showToast("error", "Terjadi kesalahan");
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ResponseModel> call, Throwable t) {
//                                        showToast("error", "Tidak ada koneksi internet");
//                                    }
//                                });
//                                dialog.dismiss();
//                            }
//                        });
//                    }
//
//
//
//
//
//                }else {
//                    showProgressBar("sds", "adad", false);
//                    showToast("error", "Gagal memuat data pegawai");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserModel> call, Throwable t) {
//
//                showProgressBar("sds", "adad", false);
//                showToast("error", "Tidak ada koneksi internet");
//
//
//            }
//        });
//
//    }




}