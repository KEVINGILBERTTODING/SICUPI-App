package com.example.sicupi.ui.main.pegawai.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sicupi.R;
import com.example.sicupi.data.api.ApiConfig;
import com.example.sicupi.data.api.PegawaiService;
import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.data.model.ResponseModel;
import com.example.sicupi.data.model.UserModel;
import com.example.sicupi.databinding.FragmentPegawaiHomeBinding;
import com.example.sicupi.ui.main.pegawai.adapter.HistoryAllCutiAdapter;
import com.example.sicupi.ui.main.pegawai.cuti.PegawaiHistoryCutiLebih14Fragment;
import com.example.sicupi.ui.main.pegawai.cuti.PegawaiHistoryCutiMelahirkanFragment;
import com.example.sicupi.ui.main.pegawai.cuti.PegawaiHistoryCutiPentingFragment;
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
        getMyProfile();
        checkCutiAktif();



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

        binding.btnCutiPenting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFragment(new PegawaiHistoryCutiPentingFragment());
            }
        });
        binding.rlDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getShowDetailCutiAktif();
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
                binding.tvJenisCuti.setText("Tidak ada cuti");
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


    private void getShowDetailCutiAktif() {
        showProgressBar("Loading", "Memuat data cuti...", true);
        pegawaiService.getShowCuti(userId).enqueue(new Callback<CutiModel>() {
            @Override
            public void onResponse(Call<CutiModel> call, Response<CutiModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showProgressBar("sdd",  "sdd", false);
                    binding.tvJenisCuti.setText(response.body().getKeterangan());
                    binding.tvTglAwal.setText(response.body().getMulaiCuti());
                    binding.tvTglSelesai.setText(response.body().getAkhirCuti());

                    Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.layout_detail_cuti);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    TextView tvJenisCuti, tvStatusCuti, tvTanggalMulai, tvTanggalSelesai, tvPerihal;
                    CardView cvStatus;
                    Button btnDownloadLampiran, btnDownloadLaporan;
                    tvJenisCuti = dialog.findViewById(R.id.tvJenisCuti);
                    tvStatusCuti = dialog.findViewById(R.id.tvStatus);
                    tvTanggalMulai = dialog.findViewById(R.id.tvTglAwal);
                    tvTanggalSelesai = dialog.findViewById(R.id.tvTglSelesai);
                    tvPerihal = dialog.findViewById(R.id.tvPerihal);
                    cvStatus = dialog.findViewById(R.id.cvCutiStatus);
                    btnDownloadLampiran = dialog.findViewById(R.id.btnDownloadLampiran);
                    btnDownloadLaporan = dialog.findViewById(R.id.btnDownloadLaporan);

                    tvJenisCuti.setText(response.body().getKeterangan());
                    tvTanggalMulai.setText(response.body().getMulaiCuti());
                    tvTanggalSelesai.setText(response.body().getAkhirCuti());
                    tvPerihal.setText(response.body().getPerihal());

                    if (response.body().getKeterangan().equals("Cuti Sakit < 14")) {
                        btnDownloadLampiran.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadSuratLampiran(
                                        String.valueOf(response.body().getCutiId()), "kurang"
                                );
                            }
                        });
                        btnDownloadLaporan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downlodLaporanCuti(Constants.URLF_DONWLOAD_LAPORAN_CUTI_SAKIT + response.body().getCutiId());

                            }
                        });
                    }else  if (response.body().getKeterangan().equals("Cuti Sakit > 14")) {
                        btnDownloadLampiran.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadSuratLampiran(
                                        String.valueOf(response.body().getCutiId()), "lebih"
                                );
                            }
                        });
                        btnDownloadLaporan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downlodLaporanCuti(Constants.URLF_DONWLOAD_LAPORAN_CUTI_SAKIT_14 + response.body().getCutiId());

                            }
                        });
                    }else  if (response.body().getKeterangan().equals("Cuti Melahirkan")) {

                        btnDownloadLampiran.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadSuratLampiran(
                                        String.valueOf(response.body().getCutiId()), "melahirkan"
                                );
                            }
                        });
                        btnDownloadLaporan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downlodLaporanCuti(Constants.URLF_DONWLOAD_LAPORAN_CUTI_MELAHIRKAN + response.body().getCutiId());

                            }
                        });
                    }else  if (response.body().getKeterangan().equals("Cuti Alasan Penting")) {
                        btnDownloadLampiran.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadSuratLampiran(
                                        String.valueOf(response.body().getCutiId()), "penting"
                                );
                            }
                        });
                        btnDownloadLaporan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downlodLaporanCuti(Constants.URLF_DONWLOAD_LAPORAN_CUTI_PENTING + response.body().getCutiId());

                            }
                        });
                    }

                    if (response.body().getVerifikasi() == 1) {
                        tvStatusCuti.setText("Disetujui");
                        btnDownloadLaporan.setVisibility(View.VISIBLE);
                        cvStatus.setCardBackgroundColor(getContext().getColor(R.color.green));
                    } else if (response.body().getVerifikasi() ==2) {
                        tvStatusCuti.setText("Ditolak");
                        btnDownloadLaporan.setVisibility(View.GONE);
                        cvStatus.setCardBackgroundColor(getContext().getColor(R.color.red));
                    }else {
                        tvStatusCuti.setText("Diproses");
                        btnDownloadLaporan.setVisibility(View.GONE);
                        cvStatus.setCardBackgroundColor(getContext().getColor(R.color.yellow));
                    }




                    dialog.show();







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

    private void downloadSuratLampiran(String cutiId, String jenis) {
        String url = Constants.URLF_DONWLOAD_LAMPIRAN_CUTI + "/" + cutiId + "/" + jenis;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void getMyProfile() {
        showProgressBar("Loading", "Memuat data...", true);
        pegawaiService.getMyProfile(userId).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showProgressBar("sds", "adad", false);


                    // jika ada pemberitahuan cuti disetujui
                    // atau cuti ditolak
                    if (response.body().getStatusPengajuan().equals("1")){ // jika cuti di setujui
                        Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.layout_alert_disetujui);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Button btnOke = dialog.findViewById(R.id.btnOke);
                        dialog.show();
                        btnOke.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pegawaiService.dismissNotif(userId).enqueue(new Callback<ResponseModel>() {
                                    @Override
                                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                        if (response.isSuccessful() && response.body().getStatus() == 200) {
                                            dialog.dismiss();
                                        }else {
                                            showToast("error", "Terjadi kesalahan");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                                        showToast("error", "Tidak ada koneksi internet");
                                    }
                                });
                                dialog.dismiss();
                            }
                        });
                    }else if (response.body().getStatusPengajuan().equals("2")){
                        Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.layout_alert_ditolak);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Button btnOke = dialog.findViewById(R.id.btnOke);
                        dialog.show();

                        btnOke.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pegawaiService.dismissNotif(userId).enqueue(new Callback<ResponseModel>() {
                                    @Override
                                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                        if (response.isSuccessful() && response.body().getStatus() == 200) {
                                            dialog.dismiss();
                                        }else {
                                            showToast("error", "Terjadi kesalahan");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                                        showToast("error", "Tidak ada koneksi internet");
                                    }
                                });
                                dialog.dismiss();
                            }
                        });
                    }





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



    private void checkCutiAktif() {
        showProgressBar("Loading", "Cek status cuti...", true);
        pegawaiService.checkCutiAktif(userId).enqueue(new Callback<CutiModel>() {
            @Override
            public void onResponse(Call<CutiModel> call, Response<CutiModel> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.layout_alert_dialog_selsesai);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Button btnOke = dialog.findViewById(R.id.btnOke);
                    dialog.show();
                    Log.d("id cuti", "onResponse: " + response.body().getCutiId());

                    btnOke.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showProgressBar("Loading", "Konfirmasi cuti selesai...", true);
                            pegawaiService.konfirmasiCutiSelesai(
                                    String.valueOf(response.body().getCutiId()),
                                    response.body().getKodePegawai()
                                    ).enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    if (response.isSuccessful() && response.body().getStatus() == 200) {
                                        showProgressBar("sdsd", "ada", false);
                                        showToast("success", "Berhasil konfirmasi cuti");
                                        dialog.dismiss();
                                    }else {
                                        showProgressBar("sdsd", "ada", false);
                                        showToast("error", "Terjadi kesalahan");

                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                    showProgressBar("sdsd", "ada", false);
                                    showToast("error", "Tidak ada koneksi internet");

                                }
                            });
                        }
                    });

                }else {

                }
            }

            @Override
            public void onFailure(Call<CutiModel> call, Throwable t) {
                showProgressBar("dsd", "sds", false);
//                showToast("error", "Tidak ada koneksi internet");

            }
        });
    }

    private void downlodLaporanCuti(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }





}