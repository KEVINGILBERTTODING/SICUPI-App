package com.example.sicupi.ui.main.pegawai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sicupi.R;
import com.example.sicupi.ui.main.pegawai.home.PegawaiHomeFragment;
import com.example.sicupi.ui.main.pegawai.profile.PegawaiProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PegawaiMainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.btmBar);

        replace(new PegawaiHomeFragment());

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuHome) {
                    replace(new PegawaiHomeFragment());
                    return true;
                } else if (item.getItemId() == R.id.menuProfile) {
                    replace(new PegawaiProfileFragment());
                    return true;
                }
                return false;
            }
        });


    }

    void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.framePegawai, fragment).commit();
    }
}