package com.example.sicupi.ui.main.pimpinan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.sicupi.R;
import com.example.sicupi.ui.main.pimpinan.home.PimpinanHomeFragment;

public class PimpinanMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pimpinan_main);

        replace(new PimpinanHomeFragment());
    }

    private void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.framePimpinan, fragment).commit();
    }
}