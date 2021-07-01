package com.barbera.barberahomesalon.Admin.Service;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberahomesalon.Admin.Network.JsonPlaceHolderApi;
import com.barbera.barberahomesalon.Admin.Network.RetrofitClientInstance;
import com.pubnub.kaushik.realtimetaxiandroiddemo.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewService extends AppCompatActivity {
    private TextView name;
    private TextView price;
    private TextView time;
    private TextView details;
    private TextView discount;
    private TextView gender;
    private TextView dod;
    private TextView type;
    private Button edit,delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_service);

        name=findViewById(R.id.servicename);
        price=findViewById(R.id.service_price);
        time=findViewById(R.id.service_time);
        details=findViewById(R.id.service_details);
        discount=findViewById(R.id.service_discount);
        gender=findViewById(R.id.service_gender);
        dod=findViewById(R.id.service_dod);
        type=findViewById(R.id.service_type);
        edit=findViewById(R.id.edit_service);
        delete=findViewById(R.id.delete_service);

        Intent intent=getIntent();

        name.setText(intent.getStringExtra("name"));
        price.setText(intent.getStringExtra("price"));
        time.setText(intent.getStringExtra("time"));
        details.setText(intent.getStringExtra("details"));
        discount.setText(intent.getStringExtra("discount"));
        type.setText(intent.getStringExtra("type"));
        boolean deal=intent.getBooleanExtra("dod",false);
        if(deal){
            dod.setText("True");
        }
        else{
            dod.setText("False");
        }
        gender.setText(intent.getStringExtra("gender"));
        String id=intent.getStringExtra("id");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ViewService.this,EditService.class);
                intent.putExtra("add","no");
                String nm=name.getText().toString();
                String pr=price.getText().toString();
                String tim=time.getText().toString();
                String det=details.getText().toString();
                String dis=discount.getText().toString();
                String typ=type.getText().toString();
                String gen=gender.getText().toString();
                String deal=dod.getText().toString();
                if(deal.equals("True")){
                    intent.putExtra("dod",true);
                }
                else{
                    intent.putExtra("dod",false);
                }
                intent.putExtra("id",id);
                intent.putExtra("name",nm);
                intent.putExtra("price",pr);
                intent.putExtra("details",det);
                intent.putExtra("time",tim);
                intent.putExtra("gender",gen);
                intent.putExtra("type",typ);
                intent.putExtra("discount",dis);

                startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit=RetrofitClientInstance.getRetrofitInstance();
                JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
                ProgressDialog progressDialog = new ProgressDialog(ViewService.this);
                progressDialog.setMessage("Hold on for a moment...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                Call<Void> call=jsonPlaceHolderApi.deleteService(id);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200){
                            progressDialog.dismiss();
                            Toast.makeText(ViewService.this,"Service deleted",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ViewService.this,ServiceActivity.class));
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(ViewService.this,"Could not delete service",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(ViewService.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewService.this,ServiceActivity.class));
    }
}