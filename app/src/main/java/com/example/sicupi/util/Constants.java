package com.example.sicupi.util;

import android.content.Intent;

public class Constants {
    public static final String SHAREDPREFNAME = "user_data";
    public static final String SHAREDPREF_LOGIN = "logged_in";
    public static final String SHAREDPRE_USER_ID = "user_id";
    public static final String SHAREDPRE_NAMA= "nama";
    public static final String SHAREDPR_ROLE= "role";
    public static final String SHAREDPR_JABATAN= "jabatan";
    public static final String IP_ADDRESS ="192.168.100.6";// ip address
    public static final String URLF_DONWLOAD_LAPORAN_CUTI_SAKIT = "http://" + IP_ADDRESS + "/pegawai/api/pegawai/downloadLaporanCutiSakit/";
    public static final String URLF_DONWLOAD_LAPORAN_CUTI_SAKIT_14 = "http://" + IP_ADDRESS + "/pegawai/api/pegawai/downloadLaporanCutiSakit14/";
    public static final String URLF_DONWLOAD_LAPORAN_CUTI_MELAHIRKAN = "http://" + IP_ADDRESS + "/pegawai/api/pegawai/downloadLaporanCutiMelahirkan/";
    public static final String URLF_DONWLOAD_LAPORAN_CUTI_PENTING = "http://" + IP_ADDRESS + "/pegawai/api/pegawai/downloadLaporanCutiPenting/";
    public static final String URLF_DONWLOAD_LAMPIRAN_CUTI = "http://" + IP_ADDRESS + "/pegawai/api/pegawai/downloadSuratLampiranCutiPegawai/";


    public static final String URL_PEGAWAI_PHOTO_PROFILE = "http://" + IP_ADDRESS + "/pegawai/assets/data/photo_profile/pegawai/";
    public static final String URL_PIMPINAN_PHOTO_PROFILE = "http://" + IP_ADDRESS + "/pegawai/assets/data/photo_profile/pimpinan/";
    public static final String URL_ADMIN_PHOTO_PROFILE = "http://" + IP_ADDRESS + "/pegawai/assets/data/photo_profile/admin/";



}
