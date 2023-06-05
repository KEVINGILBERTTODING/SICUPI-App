package com.example.sicupi.ui.main.pegawai.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sicupi.R;
import com.example.sicupi.data.api.ApiConfig;
import com.example.sicupi.data.api.PegawaiService;
import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.databinding.FragmentPegawaiHomeBinding;
import com.example.sicupi.ui.main.pegawai.adapter.HistoryAllCutiAdapter;
import com.example.sicupi.ui.main.pegawai.cuti.PegawaiHistoryCutiLebih14Fragment;
import com.example.sicupi.ui.main.pegawai.cuti.PegawaiHistoryCutiMelahirkanFragment;
import com.example.sicupi.ui.main.pegawai.cuti.PegawaiHistoryCutikurang14Fragment;
import com.example.sicupi.util.Constants;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PegawaiHomeFragment extends Fragment {

    SharedPreferences sharedPreferences;
    List<CutiModel> cutiModelList;
    LinearLayoutManager linearLayoutManager;
    HistoryAllCutiAdapter historyAllCutiAdapter;
    String userId;
    PegawaiService pegawaiService;
    AlertDialog progressDialog;

    private FragmentPegawaiHomeBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding  = FragmentPegawaiHomeBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHAREDPREFNAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constants.SHAREDPRE_USER_ID, null);
        pegawaiService = ApiConfig.getClient().create(PegawaiService.class);



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvUsername.setText(sharedPreferences.getString("nama", null));
        getAllCuti();
        getShowCutiAktif();
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
    }



    private void moveFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framePegawai, fragment)
                .addToBackStack(null).commit();
    }

    private void getAllCuti() {
        showProgressBar("Loading", "Memuat data cuti...", true);
        pegawaiService.getAllCutiByUserId(userId).enqueue(new Callback<List<CutiModel>>() {
            @Override
            public void onResponse(Call<List<CutiModel>> call, Response<List<CutiModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    showProgressBar("adad", "asdad", false);
                    cutiModelList = response.body();
                    historyAllCutiAdapter = new HistoryAllCutiAdapter(getContext(), cutiModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvCuti.setLayoutManager(linearLayoutManager);
                    binding.rvCuti.setAdapter(historyAllCutiAdapter);
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

    private void getShowCutiAktif() {
        showProgressBar("Loading", "Memuat data cuti...", true);
        pegawaiService.getShowCuti(userId).enqueue(new Callback<CutiModel>() {
            @Override
            public void onResponse(Call<CutiModel> call, Response<CutiModel> response) {
            if (response.isSuccessful() && response.body() != null) {
                showProgressBar("sdd",  "sdd", false);
                binding.tvJenisCuti.setText(response.body().getKeterangan());
                binding.tvTglAwal.setText(response.body().getMulaiCuti());
                binding.tvTglSelesai.setText(response.body().getAkhirCuti());


                if (response.body().getVerifikasi() == 1) {
                    binding.cvCutiStatus.setCardBackgroundColor(getContext().getColor(R.color.green));
                    binding.tvStatus.setText("Disetujui");
                }else {
                    binding.cvCutiStatus.setCardBackgroundColor(getContext().getColor(R.color.yellow));
                    binding.tvStatus.setText("Diproses");
                }

            }else {
                showProgressBar("sdd",  "sdd", false);
                binding.tvJenisCuti.setText("Tidak ada data");
                binding.tvTglAwal.setText("-");
                binding.tvTglSelesai.setText("-");


            }
            }

            @Override
            public void onFailure(Call<CutiModel> call, Throwable t) {
                showProgressBar("sdd",  "sdd", false);
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
}