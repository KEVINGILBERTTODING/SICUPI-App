package com.example.sicupi.data.model;

import com.example.sicupi.util.Constants;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PimpinanModel implements Serializable {
    @SerializedName("kode_pimpinan")
    String userId;
    @SerializedName("nama")
    String nama;
    @SerializedName("jabatan")
    String jabatan;
    @SerializedName("email")
    String email;
    @SerializedName("no_telp")
    String noTelp;
    @SerializedName("role")
    String role;

    @SerializedName("foto")
    String foto;

    public PimpinanModel(String userId, String foto, String nama, String jabatan, String email, String noTelp, String role) {
        this.userId = userId;
        this.nama = nama;
        this.jabatan = jabatan;
        this.email = email;
        this.noTelp = noTelp;
        this.role = role;
        this.foto = foto;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFoto() {
        return Constants.URL_PIMPINAN_PHOTO_PROFILE + foto;
    }
}
