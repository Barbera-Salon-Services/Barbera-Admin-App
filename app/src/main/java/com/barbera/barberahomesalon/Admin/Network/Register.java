package com.barbera.barberahomesalon.Admin.Network;

import com.google.gson.annotations.SerializedName;

public class Register {
    @SerializedName("phone")
    private String phone;

    @SerializedName("token")
    private String token;

    @SerializedName("otp")
    private String otp;

    @SerializedName("role")
    private String role;

    public Register(String phone, String token, String otp,String role) {
        this.phone = phone;
        this.token = token;
        this.otp = otp;
        this.role=role;
    }

    public String getOtp() {
        return otp;
    }

    public String getToken() {
        return token;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }
}
