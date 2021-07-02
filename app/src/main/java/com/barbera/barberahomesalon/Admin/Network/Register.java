package com.barbera.barberahomesalon.Admin.Network;

import com.google.gson.annotations.SerializedName;

public class Register {
    @SerializedName("phone")
    private String phone;

    @SerializedName("token")
    private String token;

    @SerializedName("otp")
    private String otp;

    public Register(String phone, String token, String otp) {
        this.phone = phone;
        this.token = token;
        this.otp = otp;
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
}
