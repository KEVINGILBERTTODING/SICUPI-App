package com.example.sicupi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sicupi.model.AuthModel;
import com.example.sicupi.util.AuthInterface;
import com.example.sicupi.util.DataApi;

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

        if (sharedPreferences.getBoolean("logged_in", false)) {
            startActivity(new Intent(LoginActivity.this, PegawaiMainActivity.class));
            finish();
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
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
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

            AuthInterface authInterface = DataApi.getClient().create(AuthInterface.class);
            authInterface.login(etEmail.getText().toString(), etPassword.getText().toString()).enqueue(new Callback<AuthModel>() {
                @Override
                public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
                    AuthModel authModel = response.body();
                    if (response.isSuccessful() && authModel.getCode() == 200) {
                        editor.putBoolean("logged_in", true);
                        editor.putString("user_id", authModel.getUserId());
                        editor.putString("nama", authModel.getNama());
                        editor.putInt("role", authModel.getRole());
                        editor.putString("jabatan", authModel.getJabatan());
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this, PegawaiMainActivity.class));
                        finish();
                        Toasty.success(LoginActivity.this, "Selamat datang " + authModel.getNama(), Toasty.LENGTH_SHORT).show();
                        progressDialog.dismiss();

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