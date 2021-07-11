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

    @SerializedName("address")
    private String address;

    @SerializedName("latitude")
    private String lat;

    @SerializedName("longitude")
    private String lon;

    public Register(String phone, String token, String otp,String role,String address,String lat,String lon) {
        this.phone = phone;
        this.token = token;
        this.otp = otp;
        this.role=role;
        this.lat=lat;
        this.address=address;
        this.lon=lon;
    }

    public String getAddress() {
        return address;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
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
