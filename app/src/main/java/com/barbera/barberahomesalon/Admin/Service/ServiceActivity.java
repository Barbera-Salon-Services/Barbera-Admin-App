package com.barbera.barberahomesalon.Admin.Service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.barbera.barberahomesalon.Admin.MainActivity;
import com.barbera.barberahomesalon.Admin.Network.JsonPlaceHolderApi;
import com.barbera.barberahomesalon.Admin.Network.RetrofitClientInstance;
import com.barbera.barberahomesalon.Admin.Network.Service;
import com.google.gson.annotations.SerializedName;
import com.pubnub.kaushik.realtimetaxiandroiddemo.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ServiceActivity extends AppCompatActivity {
    private Button add;
    private List<Service> serviceList=new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private ServiceAdapter adapter;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        add=findViewById(R.id.add_service);
        recyclerView=findViewById(R.id.service_recycler_view);
        manager=new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        serviceList=new ArrayList<>();
        adapter=new ServiceAdapter(serviceList,getApplicationContext(),0);

        SharedPreferences preferences=getSharedPreferences("Token",MODE_PRIVATE);
        token=preferences.getString("token","no");

        Retrofit retrofit= RetrofitClientInstance.getRetrofitInstance();
        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        ProgressDialog progressDialog = new ProgressDialog(ServiceActivity.this);
        progressDialog.setMessage("Hold on for a moment...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        Call<ServiceList> call=jsonPlaceHolderApi.getAllServices(token);
        call.enqueue(new Callback<ServiceList>() {
            @Override
            public void onResponse(Call<ServiceList> call, Response<ServiceList> response) {
                if(response.code()==200){
                    ServiceList serviceList1=response.body();
                    List<Service> serviceList2=serviceList1.getServiceList();
                    for(Service service:serviceList2){
                        serviceList.add(new Service(service.getName(),null,null,null,null,null,null,false,service.getId(),false));
                    }
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(ServiceActivity.this,"Could not load services",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServiceList> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ServiceActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ServiceActivity.this,EditService.class);
                intent.putExtra("add","yes");
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ServiceActivity.this, MainActivity.class));
    }
}