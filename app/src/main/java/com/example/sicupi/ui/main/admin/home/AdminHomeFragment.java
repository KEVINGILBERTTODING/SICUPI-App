package com.example.sicupi.ui.main.admin.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sicupi.R;
import com.example.sicupi.data.api.ApiConfig;
import com.example.sicupi.data.api.AdminService;
import com.example.sicupi.data.model.AdminModel;
import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.data.model.PimpinanModel;
import com.example.sicupi.data.model.UserModel;
import com.example.sicupi.databinding.FragmentAdminHomeBinding;
import com.example.sicupi.databinding.FragmentPimpinanHomeBinding;
import com.example.sicupi.ui.main.admin.adapter.UserAdapter;
import com.example.sicupi.ui.main.admin.cuti.AdminCutiMelahirkanFragment;
import com.example.sicupi.ui.main.admin.cuti.AdminCutiPentingFragment;
import com.example.sicupi.ui.main.admin.cuti.AdminCutikurang14Fragment;
import com.example.sicupi.ui.main.admin.cuti.AdminCutilebih14Fragment;
import com.example.sicupi.ui.main.admin.profile.AdminProfileFragment;
import com.example.sicupi.ui.main.admin.user.AdminInsertPegawaiFragment;
import com.example.sicupi.ui.main.pimpinan.adapter.AllPengajuanCutiAdapter;
import com.example.sicupi.ui.main.pimpinan.cuti.PimpinanHistoryCutiMelahirkanFragment;
import com.example.sicupi.ui.main.pimpinan.cuti.PimpinanHistoryCutiPentingFragment;
import com.example.sicupi.ui.main.pimpinan.cuti.PimpinanHistoryCutikurang14Fragment;
import com.example.sicupi.ui.main.pimpinan.cuti.PimpinanHistoryCutilebih14Fragment;
import com.example.sicupi.ui.main.pimpinan.profile.PimpinanProfileFragment;
import com.example.sicupi.util.Constants;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminHomeFragment extends Fragment {

    SharedPreferences sharedPreferences;
    List<UserModel>userModelList;
    LinearLayoutManager linearLayoutManager;
    UserAdapter userAdapter;
    String userId;
    AdminService adminService;
    AlertDialog progressDialog;

    private FragmentAdminHomeBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding  = FragmentAdminHomeBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHAREDPREFNAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constants.SHAREDPRE_USER_ID, null);
        adminService = ApiConfig.getClient().create(AdminService.class);
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Semua"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Tidak Pernah Cuti"));
        getMyProfile();




        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvUsername.setText(sharedPreferences.getString("nama", null));
        getAllCuti(binding.tvKurang14, "Cuti Sakit < 14");
        getAllCuti(binding.tvLebih14, "Cuti Sakit > 14");
        getAllCuti(binding.tvMelahirkan, "Cuti Melahirkan");
        getAllCuti(binding.tvPenting, "Cuti Alasan Penting");

        listener();
        getAllUser();

    }


    private void listener() {

        binding.btnCutiMelahirkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new AdminCutiMelahirkanFragment());
            }
        });

        binding.btnCutiKurang14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new AdminCutikurang14Fragment());
            }
        });
        binding.btnCutiLebih14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new AdminCutilebih14Fragment());
            }
        });

        binding.btnCutiPenting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new AdminCutiPentingFragment());
            }
        });


        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new AdminProfileFragment());
            }
        });



        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    getAllUser();
                } else if (tab.getPosition() == 1) {
                    getAllUserTidakPernahCuti();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.fabInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new AdminInsertPegawaiFragment());
            }
        });
    }



    private void moveFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAdmin, fragment)
                .addToBackStack(null).commit();

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
        adminService.getMyProfile(userId).enqueue(new Callback<AdminModel>() {
            @Override
            public void onResponse(Call<AdminModel> call, Response<AdminModel> response) {
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

                    binding.tvEmail.setText(response.body().getEmail());



                }else {
                    showProgressBar("sds", "adad", false);
                    showToast("error", "Gagal memuat data pegawai");
                }
            }

            @Override
            public void onFailure(Call<AdminModel> call, Throwable t) {

                showProgressBar("sds", "adad", false);
                showToast("error", "Tidak ada koneksi internet");


            }
        });

    }

    private void getAllUser() {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAllUsers().enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    userModelList = response.body();
                    userAdapter = new UserAdapter(getContext(), userModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvAdmin.setLayoutManager(linearLayoutManager);
                    binding.rvAdmin.setAdapter(userAdapter);
                    binding.rvAdmin.setHasFixedSize(true);
                    showProgressBar("sds","sdd", false);
                    binding.tvEmpty.setVisibility(View.GONE);
                }else {
                    showProgressBar("sds","sdd", false);
                    binding.tvEmpty.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                showProgressBar("sds","sdd", false);
                binding.tvEmpty.setVisibility(View.GONE);
                showToast("error", "Tidak ada koneksi internet");

            }
        });
    }

    private void getAllUserTidakPernahCuti() {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAllUsersTidakPernahCuti().enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    userModelList = response.body();
                    userAdapter = new UserAdapter(getContext(), userModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvAdmin.setLayoutManager(linearLayoutManager);
                    binding.rvAdmin.setAdapter(userAdapter);
                    binding.rvAdmin.setHasFixedSize(true);
                    showProgressBar("sds","sdd", false);
                    binding.tvEmpty.setVisibility(View.GONE);
                }else {
                    showProgressBar("sds","sdd", false);
                    binding.tvEmpty.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                showProgressBar("sds","sdd", false);
                binding.tvEmpty.setVisibility(View.GONE);
                showToast("error", "Tidak ada koneksi internet");

            }
        });
    }

    private void getAllCuti (TextView tvTotal, String jenis) {
        showProgressBar("Loading", "Memuat data...", true);
        adminService.getAllTotalCuti(jenis).enqueue(new Callback<List<CutiModel>>() {
            @Override
            public void onResponse(Call<List<CutiModel>> call, Response<List<CutiModel>> response) {
                if (response.body().size() > 0) {
                    showProgressBar("sdd", "Ds", false);
                    tvTotal.setText(String.valueOf(response.body().size()));

                }else {
                    tvTotal.setText("0");
                    showProgressBar("dd", "sdsd", false);
                }
            }

            @Override
            public void onFailure(Call<List<CutiModel>> call, Throwable t) {
                tvTotal.setText("-");
                showProgressBar("dd", "sdsd", false);

            }
        });


    }




}