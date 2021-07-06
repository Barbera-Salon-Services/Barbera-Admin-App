package com.barbera.barberahomesalon.Admin.Service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceList {
    @SerializedName("data")
    private List<Service> serviceList;

    public ServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }
}
