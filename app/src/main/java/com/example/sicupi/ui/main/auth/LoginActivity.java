package com.example.sicupi.ui.main.auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sicupi.R;
import com.example.sicupi.data.model.AuthModel;
import com.example.sicupi.data.api.AuthService;
import com.example.sicupi.data.api.ApiConfig;
import com.example.sicupi.ui.main.pegawai.PegawaiMainActivity;
import com.example.sicupi.ui.main.pimpinan.PimpinanMainActivity;
import com.example.sicupi.util.Constants;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText etEmail, etPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String TAG = "sdhsd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        if (sharedPreferences.getBoolean(Constants.SHAREDPREF_LOGIN, false)) {
            if (sharedPreferences.getString(Constants.SHAREDPR_ROLE, null).equals("3")){
                startActivity(new Intent(LoginActivity.this, PegawaiMainActivity.class));
                finish();
            }else if (sharedPreferences.getString(Constants.SHAREDPR_ROLE, null).equals("2")) {
                startActivity(new Intent(LoginActivity.this, PimpinanMainActivity.class));
                finish();
            }

        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void init() {
        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        sharedPreferences = getSharedPreferences(Constants.SHAREDPREFNAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void login() {
        if (etEmail.getText().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Field email tidak boleh kosong", Toasty.LENGTH_SHORT).show();
        }else if (etPassword.getText().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Field password tidak boleh kosong", Toasty.LENGTH_SHORT).show();
        }else {
            AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
            alert.setTitle("Loading").setMessage("Authorizing...").setCancelable(false);
            AlertDialog progressDialog = alert.create();
            progressDialog.show();

            AuthService authInterface = ApiConfig.getClient().create(AuthService.class);
            authInterface.login(etEmail.getText().toString(), etPassword.getText().toString()).enqueue(new Callback<AuthModel>() {
                @Override
                public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
                    AuthModel authModel = response.body();
                    if (response.isSuccessful() && authModel.getCode() == 200) {
                        
                        if (response.body().getRole() == 2) { // Pimpinan
                            editor.putBoolean(Constants.SHAREDPREF_LOGIN, true);
                            editor.putString(Constants.SHAREDPRE_USER_ID, authModel.getUserId());
                            editor.putString(Constants.SHAREDPRE_NAMA, authModel.getNama());
                            editor.putString(Constants.SHAREDPR_ROLE, String.valueOf(authModel.getRole()));
                            editor.putString(Constants.SHAREDPR_JABATAN, authModel.getJabatan());
                            editor.apply();
                            startActivity(new Intent(LoginActivity.this, PimpinanMainActivity.class));
                            finish();
                            Toasty.success(LoginActivity.this, "Selamat datang " + authModel.getNama(), Toasty.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else if (response.body().getRole() ==3){ // Pegawai
                            editor.putBoolean(Constants.SHAREDPREF_LOGIN, true);
                            editor.putString(Constants.SHAREDPRE_USER_ID, authModel.getUserId());
                            editor.putString(Constants.SHAREDPRE_NAMA, authModel.getNama());
                            editor.putString(Constants.SHAREDPR_ROLE, String.valueOf(authModel.getRole()));
                            editor.putString(Constants.SHAREDPR_JABATAN, authModel.getJabatan());
                            editor.apply();
                            startActivity(new Intent(LoginActivity.this, PegawaiMainActivity.class));
                            finish();
                            Toasty.success(LoginActivity.this, "Selamat datang " + authModel.getNama(), Toasty.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }


                    }else {
                        Toasty.error(getApplicationContext(), authModel.getMessage(), Toasty.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<AuthModel> call, Throwable t) {
                    Toasty.error(getApplicationContext(), "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Log.e(TAG, "onFailure: ", t);

                }
            });
        }
    }


}