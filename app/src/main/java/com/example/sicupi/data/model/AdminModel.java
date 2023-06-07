package com.example.sicupi.data.model;

import com.example.sicupi.util.Constants;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdminModel implements Serializable {

    @SerializedName("email")
    String email;
    @SerializedName("nama")
    String nama;
    @SerializedName("password")
    String password;
    @SerializedName("foto")
    String foto;

    public AdminModel(String email, String foto, String nama, String password) {
        this.email = email;
        this.nama = nama;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getNama() {
        return nama;
    }

    public String getPassword() {
        return password;
    }

    public String getFoto() {
        return Constants.URL_ADMIN_PHOTO_PROFILE + foto;
    }
}
