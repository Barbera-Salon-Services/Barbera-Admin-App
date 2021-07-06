package com.barbera.barberahomesalon.Admin.Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberahomesalon.Admin.Network.JsonPlaceHolderApi;
import com.barbera.barberahomesalon.Admin.Network.RetrofitClientInstance;
import com.pubnub.kaushik.realtimetaxiandroiddemo.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceHolder> {
    private List<Service> list;
    private Context activity;
    private int flag;
    private String token;


    public ServiceAdapter(List<Service> list, Context activity, int flag) {
        this.list = list;
        this.activity = activity;
        this.flag = flag;
    }

    @NonNull
    @Override
    public ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item,parent,false);
        return new ServiceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAdapter.ServiceHolder holder, int position) {
        Service service=list.get(position);
        holder.name.setText(service.getName());
        holder.id=service.getId();
        SharedPreferences preferences=activity.getSharedPreferences("Token",activity.MODE_PRIVATE);
        token=preferences.getString("token","no");

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit= RetrofitClientInstance.getRetrofitInstance();
                JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
//                ProgressDialog progressDialog = new ProgressDialog(activity);
//                progressDialog.setMessage("Hold on for a moment...");
//                progressDialog.show();
//                progressDialog.setCancelable(false);
                Call<ServiceItem> call=jsonPlaceHolderApi.getService(service.getId(),token);
                call.enqueue(new Callback<ServiceItem>() {
                    @Override
                    public void onResponse(Call<ServiceItem> call, Response<ServiceItem> response) {
                        if(response.code()==200){
                            ServiceItem serviceItem=response.body();
                            Service service1=serviceItem.getService();
                            holder.price=service1.getPrice();
                            holder.time=service1.getTime();
                            holder.subtype=service1.getSubtype();
                            holder.details=service1.getDetail();
                            holder.discount=service1.getCutprice();
                            holder.gender=service1.getGender();
                            holder.type=service1.getType();
                            holder.dod=service1.isDod();
                            holder.trend=service1.isTrend();
                            Intent intent=new Intent(activity,ViewService.class);
                            intent.putExtra("name",holder.name.getText().toString());
                            intent.putExtra("price",holder.price);
                            intent.putExtra("time",holder.time);
                            intent.putExtra("type",holder.type);
                            intent.putExtra("subtype",holder.subtype);
                            intent.putExtra("gender",holder.gender);
                            intent.putExtra("details",holder.details);
                            intent.putExtra("discount",holder.discount);
                            intent.putExtra("dod",holder.dod);
                            intent.putExtra("id",holder.id);
                            intent.putExtra("trend",holder.trend);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //progressDialog.dismiss();
                            activity.startActivity(intent);
                        }
                        else{
                            //progressDialog.dismiss();
                            Toast.makeText(activity.getApplicationContext(),"Could not load service",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceItem> call, Throwable t) {
                        //progressDialog.dismiss();
                        Toast.makeText(activity.getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class ServiceHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private CardView item;
        private Button add;
        private String price,details,time,discount,gender,type,id,subtype;
        private boolean dod,trend;

//        private ImageView del;
        public ServiceHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.service_name);
            item=itemView.findViewById(R.id.item_ser);
            add=itemView.findViewById(R.id.add_service);
//            del=itemView.findViewById(R.id.delete_service);
        }
    }

}
