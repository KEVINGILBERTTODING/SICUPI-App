package com.example.sicupi.ui.main.pegawai.profile;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sicupi.R;
import com.example.sicupi.data.api.ApiConfig;
import com.example.sicupi.data.api.PegawaiService;
import com.example.sicupi.data.model.CutiModel;
import com.example.sicupi.data.model.ResponseModel;
import com.example.sicupi.data.model.UserModel;
import com.example.sicupi.databinding.FragmentPegawaiProfileBinding;
import com.example.sicupi.ui.main.auth.LoginActivity;
import com.example.sicupi.util.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PegawaiProfileFragment extends Fragment {
    private FragmentPegawaiProfileBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    PegawaiService pegawaiService;
    AlertDialog progressDialog;
    String userId;
    private File file;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPegawaiProfileBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHAREDPREFNAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constants.SHAREDPRE_USER_ID, null);
        editor = sharedPreferences.edit();
        pegawaiService = ApiConfig.getClient().create(PegawaiService.class);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTotalCuti(1, binding.tvSetuju);
        getTotalCuti(2, binding.tvTolak);
        getMyProfile();
        listener();


    }

    private void listener() {
        binding.rvUbahProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framePegawai, new PegawaiEditProfileFragment())
                        .addToBackStack(null).commit();
            }
        });
        binding.rvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });


        binding.btnSimpanPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePhoto();
            }
        });
    }

    private void getMyProfile() {
        showProgressBar("Loading", "Memuat data...", true);
        pegawaiService.getMyProfile(userId).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showProgressBar("sds", "adad", false);
                    binding.tvNama.setText(response.body().getNama());
                    binding.tvJabatan.setText(response.body().getJabatan());

                    Glide.with(getContext())
                            .load(response.body().getFoto())
                            .centerInside()
                            .centerCrop()
                            .fitCenter()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(binding.profileImage);


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

    private void getTotalCuti(Integer verifikasi, TextView tvTotal) {
        showProgressBar("Loading", "Memuat data...", true);
        pegawaiService.totalCutiModelProfile(userId, verifikasi).enqueue(new Callback<List<CutiModel>>() {
            @Override
            public void onResponse(Call<List<CutiModel>> call, Response<List<CutiModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    tvTotal.setText(String.valueOf(response.body().size()));
                    showProgressBar("dsdsd", "sdsds", false);
                }else {
                    tvTotal.setText("0");
                    showProgressBar("dsdsd", "sdsds", false);
                }
            }

            @Override
            public void onFailure(Call<List<CutiModel>> call, Throwable t) {
                tvTotal.setText("-");
                showProgressBar("dsdsd", "sdsds", false);
                showToast("error", "Tidak ada koneksi internet");

            }
        });
    }

    private void logOut() {
        editor.clear().apply();
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                String pdfPath = getRealPathFromUri(uri);
                file = new File(pdfPath);
                binding.profileImage.setImageBitmap(BitmapFactory.decodeFile(pdfPath));
                binding.btnSimpanPhoto.setVisibility(View.VISIBLE);

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

    private void updatePhoto() {
        showProgressBar("Loading", "Menyimpan perubahan...", true);
        HashMap map = new HashMap();
        map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), userId));
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part foto = MultipartBody.Part.createFormData("foto", file.getName(), requestBody);
        pegawaiService.updatePhotoProfile(map, foto).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body().getStatus() == 200) {
                    showProgressBar("fdsf", "fsdf", false);
                    showToast("success", "Berhasil mengubah foto profil");

                }else {
                    showProgressBar("fdsf", "fsdf", false);
                    showToast("error", "Gagal mengubah foto profil");
                }
            }

            @Override
            public void onFailure(Call <ResponseModel>call, Throwable t) {
                showProgressBar("fdsf", "fsdf", false);
                showToast("error", "Tidak ada koneksi internet");
            }
        });
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
}