package com.barbera.barberahomesalon.Admin.Service;

import com.google.gson.annotations.SerializedName;

public class ServiceItem {
    @SerializedName("data")
    private Service service;

    public ServiceItem(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }
}
