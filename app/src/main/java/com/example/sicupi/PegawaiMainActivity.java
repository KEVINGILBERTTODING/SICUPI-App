package com.example.sicupi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.sicupi.PegawaiFragment.PegawaiHomeFragment;

public class PegawaiMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // jalankan home fragment
        replace(new PegawaiHomeFragment());


    }

    void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.framePegawai, fragment).commit();
    }
}