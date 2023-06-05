package com.example.sicupi.ui.main.pegawai.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sicupi.R;
import com.example.sicupi.databinding.FragmentPegawaiProfileBinding;
import com.example.sicupi.ui.main.auth.LoginActivity;
import com.example.sicupi.util.Constants;

public class PegawaiProfileFragment extends Fragment {
    private FragmentPegawaiProfileBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPegawaiProfileBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHAREDPREFNAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();


    }

    private void listener() {

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear().apply();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();

            }
        });
    }

    private void logOut() {

    }
}