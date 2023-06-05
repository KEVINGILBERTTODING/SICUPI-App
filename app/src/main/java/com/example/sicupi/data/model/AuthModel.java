package com.example.sicupi.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AuthModel implements Serializable {
    @SerializedName("code")
    Integer code;
    @SerializedName("user_id")
    String userId;
    @SerializedName("nama")
    String nama;
    @SerializedName("role")
    Integer role;
    @SerializedName("jabatan")
    String jabatan;
    @SerializedName("message")
    String message;
    public AuthModel(Integer code, String message, String userId, String nama, Integer role, String jabatan) {
        this.code = code;
        this.userId = userId;
        this.nama = nama;
        this.role = role;
        this.jabatan = jabatan;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getUserId() {
        return userId;
    }

    public String getNama() {
        return nama;
    }

    public Integer getRole() {
        return role;
    }

    public String getJabatan() {
        return jabatan;
    }

    public String getMessage() {
        return message;
    }
}
