package com.barbera.barberahomesalon.Admin.Service;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberahomesalon.Admin.Network.JsonPlaceHolderApi;
import com.barbera.barberahomesalon.Admin.Network.RetrofitClientInstance;
import com.barbera.barberahomesalon.Admin.Network.Service;
import com.pubnub.kaushik.realtimetaxiandroiddemo.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditService extends AppCompatActivity {
    private EditText price;
    private EditText time;
    private EditText details;
    private EditText discount;
    private EditText gender;
    private CheckBox dod_true,dod_false;
    private EditText type;
    private EditText name;
    private TextView nameT;
    private Button save;
    private String name1,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);

        price=findViewById(R.id.edit_price);
        time=findViewById(R.id.edit_time);
        details=findViewById(R.id.edit_details);
        discount=findViewById(R.id.edit_discount);
        gender=findViewById(R.id.edit_gender);
        dod_false=findViewById(R.id.dod_false);
        dod_true=findViewById(R.id.dod_true);
        type=findViewById(R.id.edit_type);
        save=findViewById(R.id.save_service);
        nameT=findViewById(R.id.nameT);
        name=findViewById(R.id.edit_name);

        Intent intent=getIntent();
        String add=intent.getStringExtra("add");

        if(add.equals("no")){
            name.setVisibility(View.GONE);
            nameT.setVisibility(View.GONE);
            name1=intent.getStringExtra("name");
            id=intent.getStringExtra("id");
            String price1=intent.getStringExtra("price");
            String details1=intent.getStringExtra("details");
            String time1=intent.getStringExtra("time");
            String gender1=intent.getStringExtra("gender");
            String type1=intent.getStringExtra("type");
            String discount1=intent.getStringExtra("discount");
            boolean dod1=intent.getBooleanExtra("dod",false);
            price.setText(price1);
            details.setText(details1);
            gender.setText(gender1);
            time.setText(time1);
            discount.setText(discount1);
            type.setText(type1);
            if(dod1){
                dod_true.setChecked(true);
                dod_false.setChecked(false);
            }
            else{
                dod_true.setChecked(false);
                dod_false.setChecked(true);
            }
        }
        else{
            name.setVisibility(View.VISIBLE);
            nameT.setVisibility(View.VISIBLE);
        }
        dod_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dod_true.setChecked(true);
                dod_false.setChecked(false);
            }
        });
        dod_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dod_true.setChecked(false);
                dod_false.setChecked(true);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pr=price.getText().toString();
                String det=details.getText().toString();
                String tm=time.getText().toString();
                String ty=type.getText().toString();
                String dis=discount.getText().toString();
                String gen=gender.getText().toString();

                if(!dod_true.isChecked() && !dod_false.isChecked()){
                    Toast.makeText(getApplicationContext(),"Please select true or false",Toast.LENGTH_SHORT).show();
                }
                else if(!gen.equals("male") && !gen.equals("female")){
                    gender.setError("Enter male or female");
                }
                else if(pr.isEmpty()){
                    price.setError("Enter price");
                }
                else if(dis.isEmpty()){
                    price.setError("Enter discounted price");
                }
                else if(tm.isEmpty()){
                    price.setError("Enter time");
                }
                else if(ty.isEmpty()){
                    price.setError("Enter type of service");
                }
//                else if(det.isEmpty()){
//                    price.setError("Enter details");
//                }

                else{
                    boolean dod;
                    if(dod_true.isChecked()){
                        dod=true;
                    }
                    else{
                        dod=false;
                    }
                    Retrofit retrofit= RetrofitClientInstance.getRetrofitInstance();
                    JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
                    if(add.equals("yes")){
                        ProgressDialog progressDialog = new ProgressDialog(EditService.this);
                        progressDialog.setMessage("Hold on for a moment...");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        Call<Service> call=jsonPlaceHolderApi.addService(new Service(name.getText().toString(),price.getText().toString(),time.getText().toString(),details.getText().toString(),discount.getText().toString(),gender.getText().toString(),type.getText().toString(),dod,null));
                        call.enqueue(new Callback<Service>() {
                            @Override
                            public void onResponse(Call<Service> call, Response<Service> response) {
                                if(response.code()==200){
                                    progressDialog.dismiss();
                                    Toast.makeText(EditService.this,"Service added",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditService.this,ServiceActivity.class));
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(EditService.this,"Could not add service",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Service> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(EditService.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        ProgressDialog progressDialog = new ProgressDialog(EditService.this);
                        progressDialog.setMessage("Hold on for a moment...");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        Call<Service> call=jsonPlaceHolderApi.updateService(new Service(name1,pr,tm,det,dis,gen,ty,dod,id));
                        call.enqueue(new Callback<Service>() {
                            @Override
                            public void onResponse(Call<Service> call, Response<Service> response) {
                                if(response.code()==200){
                                    progressDialog.dismiss();
                                    Toast.makeText(EditService.this,"Service updated",Toast.LENGTH_SHORT).show();
                                    Intent intent1=new Intent(EditService.this,ViewService.class);
                                    intent1.putExtra("price",pr);
                                    intent1.putExtra("details",det);
                                    intent1.putExtra("time",tm);
                                    intent1.putExtra("type",ty);
                                    intent1.putExtra("gender",gen);
                                    intent1.putExtra("dod",dod);
                                    intent1.putExtra("discount",dis);
                                    intent1.putExtra("name",name1);
                                    intent1.putExtra("id",id);
                                    startActivity(intent1);
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(EditService.this,"Could not update service",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Service> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(EditService.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                }
            }
        });

    }
}