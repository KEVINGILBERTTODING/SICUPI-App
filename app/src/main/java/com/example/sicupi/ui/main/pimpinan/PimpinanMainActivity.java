package com.example.sicupi.ui.main.pimpinan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sicupi.R;
import com.example.sicupi.ui.main.pimpinan.home.PimpinanHomeFragment;
import com.example.sicupi.ui.main.pimpinan.profile.PimpinanProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PimpinanMainActivity extends AppCompatActivity {
    BottomNavigationView btmbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pimpinan_main);
        btmbar = findViewById(R.id.btmBar);

        replace(new PimpinanHomeFragment());

        btmbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuHome) {
                    replace(new PimpinanHomeFragment());
                    return true;
                }else  if (item.getItemId() == R.id.menuProfile) {
                    replace(new PimpinanProfileFragment());
                    return true;
                }
                return false;
            }
        });


    }

    private void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.framePimpinan, fragment).commit();
    }
}