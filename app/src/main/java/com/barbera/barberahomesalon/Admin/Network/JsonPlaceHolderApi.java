package com.barbera.barberahomesalon.Admin.Network;

import com.barbera.barberahomesalon.Admin.Service.ServiceItem;
import com.barbera.barberahomesalon.Admin.Service.ServiceList;

import java.util.List;
import java.util.ServiceLoader;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {
    @POST("addservice")
    Call<Service> addService(@Body Service service);

    @DELETE("delservice/{id}")
    Call<Void> deleteService(@Path("id") String id);

    @GET("getservbyid/{serviceid}")
    Call<ServiceItem> getService(@Path("serviceid") String id);

    @GET("getallservname")
    Call<ServiceList> getAllServices();

    @POST("updateservice")
    Call<Service> updateService(@Body Service service);

}
