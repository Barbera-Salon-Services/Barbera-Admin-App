package com.barbera.barberahomesalon.Admin.Network;

import com.barbera.barberahomesalon.Admin.Service.ServiceItem;
import com.barbera.barberahomesalon.Admin.Service.ServiceList;

import java.util.List;
import java.util.ServiceLoader;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {
    @POST("addservice")
    Call<Service> addService(@Body Service service,@Header("token") String token);

    @DELETE("delservice/{id}")
    Call<Void> deleteService(@Path("id") String id,@Header("token") String token);

    @GET("getservbyid/{serviceid}")
    Call<ServiceItem> getService(@Path("serviceid") String id,@Header("token") String token);

    @GET("getallservname")
    Call<ServiceList> getAllServices(@Header("token") String token);

    @POST("updateservice")
    Call<Service> updateService(@Body Service service,@Header("token") String token);

    @POST("loginphone")
    Call<Register> getToken(@Body Register register);

    @POST("loginotp")
    Call<Register> checkOtp(@Body Register register, @Header("token") String token);

}
