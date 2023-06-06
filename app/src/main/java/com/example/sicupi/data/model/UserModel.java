package com.example.sicupi.data.model;

import com.example.sicupi.util.Constants;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Serializable
{
    @SerializedName("kode_pegawai")
    String kodePegawai;

    @SerializedName("nik")
    String nik;

    @SerializedName("nama")
    String nama;

    @SerializedName("jabatan")
    String jabatan;

    @SerializedName("jenis_kel")
    String jenisKelamin;

    @SerializedName("no_telp")
    String nomorTelepon;

    @SerializedName("alamat")
    String alamat;

    @SerializedName("email")
    String email;

    @SerializedName("password")
    String password;

    @SerializedName("foto")
    String foto;

    @SerializedName("status")
    String status;

    @SerializedName("role")
    String role;

    @SerializedName("cuti")
    String cuti;
    @SerializedName("tgl_masuk")
    String tglMasuk;
    @SerializedName("status_pengajuan")
    String statusPengajuan;
    public UserModel(String kodePegawai, String statusPengajuan, String tglMasuk, String nik, String nama, String jabatan, String jenisKelamin, String nomorTelepon, String alamat, String email, String password, String foto, String status, String role, String cuti) {
        this.kodePegawai = kodePegawai;
        this.nik = nik;
        this.nama = nama;
        this.jabatan = jabatan;
        this.jenisKelamin = jenisKelamin;
        this.nomorTelepon = nomorTelepon;
        this.alamat = alamat;
        this.email = email;
        this.password = password;
        this.foto = foto;
        this.status = status;
        this.role = role;
        this.tglMasuk = tglMasuk;
        this.cuti = cuti;
        this.statusPengajuan = statusPengajuan;
    }

    public String getKodePegawai() {
        return kodePegawai;
    }

    public void setKodePegawai(String kodePegawai) {
        this.kodePegawai = kodePegawai;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
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

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFoto() {
        return Constants.URL_PEGAWAI_PHOTO_PROFILE + foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCuti() {
        return cuti;
    }

    public void setCuti(String cuti) {
        this.cuti = cuti;
    }

    public String getTglMasuk() {
        return tglMasuk;
    }

    public void setTglMasuk(String tglMasuk) {
        this.tglMasuk = tglMasuk;
    }

    public String getStatusPengajuan() {
        return statusPengajuan;
    }

    public void setStatusPengajuan(String statusPengajuan) {
        this.statusPengajuan = statusPengajuan;
    }
}
