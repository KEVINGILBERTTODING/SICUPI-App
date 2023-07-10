package com.example.sicupi.ui.main.pegawai.cuti;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sicupi.R;
import com.example.sicupi.data.api.ApiConfig;
import com.example.sicupi.data.api.PegawaiService;
import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.data.model.ResponseModel;
import com.example.sicupi.data.model.UserModel;
import com.example.sicupi.databinding.FragmentPegawaiHistoryCutiMelahirkanBinding;
import com.example.sicupi.ui.main.pegawai.adapter.HistoryAllCutiAdapter;
import com.example.sicupi.util.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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

public class PegawaiHistoryCutiMelahirkanFragment extends Fragment {

    private FragmentPegawaiHistoryCutiMelahirkanBinding binding;
    SharedPreferences sharedPreferences;
    private AlertDialog progressDialog;
    String userId;
    HistoryAllCutiAdapter historyAllCutiAdapter;
    List<CutiModel> cutiModelList;
    LinearLayoutManager linearLayoutManager;
    PegawaiService pegawaiService;
    EditText etPdfPath;
    private Boolean totalCuti;
    String cuti;
    private File file;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPegawaiHistoryCutiMelahirkanBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHAREDPREFNAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constants.SHAREDPRE_USER_ID, null);
        pegawaiService = ApiConfig.getClient().create(PegawaiService.class);
        getMyProfile();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData("Cuti Melahirkan");
        checkTotalCuti();

        listener();




    }
    private void getData(String keterangan) {
        showProgressBar("Loading", "Memuat data...", true);
        pegawaiService.getCutiByUserId(userId, keterangan).enqueue(new Callback<List<CutiModel>>() {
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

        binding.fabInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (totalCuti == false) {
                    showDialogWarning("Jumlah cuti anda pada tahun ini sudah melebihi batas maksimal, mohon menunggu sampai tahun depan.");
                }else if (cuti.equals("2")) { // ada cuti lagi di proses
                    showDialogWarning("Anda telah mengajukan cuti, mohon menunggu verifikasi dari admin.");
                }else if (cuti.equals("1")) { // sedang dalam massa cuti
                    showDialogWarning("Status cuti anda masih aktif, mohon menunggu sampai masa cuti selesai.");
                }else {


                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.layout_insert_cuti_melahirkan);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                TextView tvWaktuMulai, tvWaktuSelesai;
                Button btnSubmit, btnPdfPicker;
                btnSubmit = dialog.findViewById(R.id.btnSubmit);
                tvWaktuMulai = dialog.findViewById(R.id.tvWaktuMulai);
                tvWaktuSelesai = dialog.findViewById(R.id.tvWaktuSelesai);
                etPdfPath = dialog.findViewById(R.id.etPdfPath);
                btnPdfPicker = dialog.findViewById(R.id.btnPdfPicker);

                dialog.show();

                Button btnBatal = dialog.findViewById(R.id.btnBatal);
                btnBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tvWaktuMulai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDatePicker(tvWaktuMulai);
                    }
                });

                tvWaktuSelesai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDatePicker(tvWaktuSelesai);
                    }
                });

                btnPdfPicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/pdf");
                        startActivityForResult(intent, 1);
                    }
                });

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get tanggal sekarang jakarta
                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"));
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String tglSekarang = dateFormat.format(calendar.getTime());

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate tanggalMulai = LocalDate.parse(tvWaktuMulai.getText().toString(), formatter);
                        LocalDate tanggalSelesai = LocalDate.parse(tvWaktuSelesai.getText().toString(), formatter);

                        Period selisih = Period.between(tanggalMulai, tanggalSelesai);



                        if (tvWaktuMulai.getText().toString().isEmpty()) {
                            tvWaktuMulai.setError("Tidak boleh kosong");
                        }else if (tvWaktuSelesai.getText().toString().isEmpty()) {
                            tvWaktuSelesai.setError("Tidak boleh kosong");
                        }else if (etPdfPath.getText().toString().isEmpty()) {
                            showToast("error", "Anda belum memilih surat lampiran cuti");
                        }
                        // jika tanggal cuti lebih besar dari tanggal selesai
                        else if (tvWaktuMulai.getText().toString().compareTo(tvWaktuSelesai.getText().toString()) > 0) {
                            tvWaktuMulai.setError("Tanggal cuti tidak boleh lebih kecil dari tanggal sekarang");
                            showToast("error", "Tanggal mulai tidak boleh lebih besar dari tanggal selesai");
                            tvWaktuMulai.requestFocus();
                            return;
                        }

                        // jika tanggal cuti lebih kecil dari tanggal sekarang
                        else if (tvWaktuMulai.getText().toString().compareTo(tglSekarang) < 0) {
                            tvWaktuMulai.setError("Tanggal cuti tidak boleh lebih kecil dari tanggal sekarang");
                            showToast("error", "Tanggal mulai tidak boleh lebih kecil dari tanggal sekarang");
                            tvWaktuMulai.requestFocus();
                            return;
                        }else if (selisih.toTotalMonths() > 3) {
                            tvWaktuSelesai.setError("Tanggal cuti tidak boleh lebih dari 3 bulan");
                            showToast("error", "Tanggal cuti tidak boleh lebih dari 3 bulan");
                            tvWaktuSelesai.requestFocus();
                        }else {
                            showProgressBar("Loading", "Menyimmpan data...", true);

                            HashMap map = new HashMap();
                            map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), userId));
                            map.put("tgl_cuti", RequestBody.create(MediaType.parse("text/plain"), tvWaktuMulai.getText().toString()));
                            map.put("tgl_selesai", RequestBody.create(MediaType.parse("text/plain"),  tvWaktuSelesai.getText().toString()));

                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/pdf"), file);
                            MultipartBody.Part fileLampiran = MultipartBody.Part.createFormData("lampiran", file.getName(), requestBody);


                            pegawaiService.inserCutiMelahirkan(map, fileLampiran).enqueue(new Callback<ResponseModel> () {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    if (response.isSuccessful() && response.body().getStatus() == 200 ) {
                                        showProgressBar("sd", "sds", false);
                                        dialog.dismiss();
                                        showToast("success", "Berhasil mengajukan Cuti");
                                        getData("Cuti Melahirkan");
                                        getMyProfile();
                                    }else {
                                        showProgressBar("sds", "dsds", false);
                                        showToast("error", response.body().getMessage());
                                    }
                                }

                                @Override
                                public void onFailure(Call <ResponseModel> call, Throwable t) {
                                    showProgressBar("sds", "dsds", false);
                                    showToast("error", "Tidak ada koneksi internet");
                                    dialog.dismiss();

                                }
                            });



                        }
                    }
                });


            }
            }
        });


    }

    private void getDatePicker(TextView tvDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateFormatted, monthFormatted;

                if (month < 10) {
                    monthFormatted = String.format("%02d", month + 1);
                }else {
                    monthFormatted = String.valueOf(month + 1);
                }

                if (dayOfMonth < 10) {
                    dateFormatted = String.format("%02d", dayOfMonth);
                }else {
                    dateFormatted = String.valueOf(dayOfMonth);
                }

                tvDate.setText(year + "-" + monthFormatted + "-" + dateFormatted);
            }
        });
        datePickerDialog.show();
    }

    private void getMyProfile() {
        showProgressBar("Loading", "Memuat data...", true);
        pegawaiService.getMyProfile(userId).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showProgressBar("sds", "adad", false);
                    cuti = response.body().getCuti();




                }else {
                    showProgressBar("sds", "adad", false);
                    showToast("error", "Gagal memuat data pegawai");
                    binding.fabInsert.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

                showProgressBar("sds", "adad", false);
                showToast("error", "Tidak ada koneksi internet");
                binding.fabInsert.setEnabled(false);

            }
        });

    }


    // function untuk menghitung jumlah cuti

    private void checkTotalCuti() {
        showProgressBar("Loading", "Memuat data...", true);
        pegawaiService.verifiedTotalCutiMelahirkan(userId).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                showProgressBar("s", "s", false);
                if (response.isSuccessful() && response.body() != null) {
                    Integer jumlahCuti = Integer.parseInt(response.body().getMessage());
                    if (jumlahCuti >= 1) {
                        totalCuti = false;
                    }else {

                        totalCuti = true;

                    }



                }else {
                    showToast("err", "Terjadi kesalahan");
                    binding.fabInsert.setEnabled(false);

                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("s", "s", false);
                showToast("err", "Tidak ada koneksi internet");
                binding.fabInsert.setEnabled(false);



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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                String pdfPath = getRealPathFromUri(uri);
                file = new File(pdfPath);
                etPdfPath.setText(file.getName());

            }
        }
    }


    public String getRealPathFromUri(Uri uri) {
        String filePath = "";
        if (getContext().getContentResolver() != null) {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                File file = new File(getContext().getCacheDir(), getFileName(uri));
                writeFile(inputStream, file);
                filePath = file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void writeFile(InputStream inputStream, File file) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    private void showDialogWarning(String message) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.layout_alert_process);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnOke = dialog.findViewById(R.id.btnOke);
        TextView tvAlertMessage = dialog.findViewById(R.id.tvMessageAlert);
        tvAlertMessage.setText(message);
        dialog.show();

        btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
}