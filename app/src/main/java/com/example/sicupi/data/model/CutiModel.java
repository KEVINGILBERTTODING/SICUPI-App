package com.example.sicupi.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CutiModel implements Serializable {
    @SerializedName("id_cuti")
    Integer cutiId;

    @SerializedName("kode_pegawai")
    private String kodePegawai;

    @SerializedName("nik")
    private String nik;

    @SerializedName("nama")
    private String nama;

    @SerializedName("tanggal_pengajuan")
    private String tanggalPengajuan;

    @SerializedName("mulai_cuti")
    private String mulaiCuti;

    @SerializedName("akhir_cuti")
    private String akhirCuti;

    @SerializedName("surat_dokter")
    private String suratDokter;

    @SerializedName("surat_dokter14")
    private String suratDokter14;

    @SerializedName("surat_melahirkan")
    private String suratMelahirkan;

    @SerializedName("surat_alasanpenting")
    private String suratAlasanPenting;

    @SerializedName("keterangan")
    private String keterangan;

    @SerializedName("verifikasi")
    private Integer verifikasi;

    @SerializedName("perihal")
    private String perihal;

    @SerializedName("status")
    private Integer status;

    public CutiModel(Integer cutiId, String kodePegawai, String nik, String nama, String tanggalPengajuan, String mulaiCuti, String akhirCuti, String suratDokter, String suratDokter14, String suratMelahirkan, String suratAlasanPenting, String keterangan, Integer verifikasi, String perihal, Integer status) {
        this.cutiId = cutiId;
        this.kodePegawai = kodePegawai;
        this.nik = nik;
        this.nama = nama;
        this.tanggalPengajuan = tanggalPengajuan;
        this.mulaiCuti = mulaiCuti;
        this.akhirCuti = akhirCuti;
        this.suratDokter = suratDokter;
        this.suratDokter14 = suratDokter14;
        this.suratMelahirkan = suratMelahirkan;
        this.suratAlasanPenting = suratAlasanPenting;
        this.keterangan = keterangan;
        this.verifikasi = verifikasi;
        this.perihal = perihal;
        this.status = status;
    }

    public Integer getCutiId() {
        return cutiId;
    }

    public void setCutiId(Integer cutiId) {
        this.cutiId = cutiId;
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

    public String getTanggalPengajuan() {
        return tanggalPengajuan;
    }

    public void setTanggalPengajuan(String tanggalPengajuan) {
        this.tanggalPengajuan = tanggalPengajuan;
    }

    public String getMulaiCuti() {
        return mulaiCuti;
    }

    public void setMulaiCuti(String mulaiCuti) {
        this.mulaiCuti = mulaiCuti;
    }

    public String getAkhirCuti() {
        return akhirCuti;
    }

    public void setAkhirCuti(String akhirCuti) {
        this.akhirCuti = akhirCuti;
    }

    public String getSuratDokter() {
        return suratDokter;
    }

    public void setSuratDokter(String suratDokter) {
        this.suratDokter = suratDokter;
    }

    public String getSuratDokter14() {
        return suratDokter14;
    }

    public void setSuratDokter14(String suratDokter14) {
        this.suratDokter14 = suratDokter14;
    }

    public String getSuratMelahirkan() {
        return suratMelahirkan;
    }

    public void setSuratMelahirkan(String suratMelahirkan) {
        this.suratMelahirkan = suratMelahirkan;
    }

    public String getSuratAlasanPenting() {
        return suratAlasanPenting;
    }

    public void setSuratAlasanPenting(String suratAlasanPenting) {
        this.suratAlasanPenting = suratAlasanPenting;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public Integer getVerifikasi() {
        return verifikasi;
    }

    public void setVerifikasi(Integer verifikasi) {
        this.verifikasi = verifikasi;
    }

    public String getPerihal() {
        return perihal;
    }

    public void setPerihal(String perihal) {
        this.perihal = perihal;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
