package com.example.sicupi.ui.main.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.example.sicupi.R;
import com.example.sicupi.ui.main.admin.home.AdminHomeFragment;
import com.example.sicupi.ui.main.admin.profile.AdminProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminMainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        bottomNavigationView = findViewById(R.id.btmBar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuHome) {
                    replace(new AdminHomeFragment());
                    return true;
                }else if (item.getItemId() == R.id.menuProfile) {
                    replace(new AdminProfileFragment());
                    return true;
                }

                return false;
            }
        });

        replace(new AdminHomeFragment());
    }

    private void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameAdmin, fragment).commit();
    }
}