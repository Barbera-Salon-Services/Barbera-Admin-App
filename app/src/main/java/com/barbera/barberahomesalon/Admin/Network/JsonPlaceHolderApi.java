package com.barbera.barberahomesalon.Admin.Network;

import androidx.core.provider.FontsContractCompat;

import com.barbera.barberahomesalon.Admin.Service.Service;
import com.barbera.barberahomesalon.Admin.Service.ServiceItem;
import com.barbera.barberahomesalon.Admin.Service.ServiceList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {
    @Multipart
    @POST("addservice")
    Call<Void> addService(@Part MultipartBody.Part file, @Header("Authorization") String token);

    @DELETE("delservice/{id}")
    Call<Void> deleteService(@Path("id") String id,@Header("Authorization") String token);

    @GET("getservbyid/{serviceid}")
    Call<ServiceItem> getService(@Path("serviceid") String id,@Header("Authorization") String token);

    @GET("getallservname")
    Call<ServiceList> getAllServices(@Header("Authorization") String token);

    @POST("updateservice")
    Call<Service> updateService(@Body Service service,@Header("Authorization") String token);

    @POST("loginphone")
    Call<Register> getToken(@Body Register register);

    @POST("loginotp")
    Call<Register> checkOtp(@Body Register register, @Header("Authorization") String token);

}
