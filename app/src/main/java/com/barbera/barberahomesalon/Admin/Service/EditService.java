package com.barbera.barberahomesalon.Admin.Service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberahomesalon.Admin.Network.JsonPlaceHolderApi;
import com.barbera.barberahomesalon.Admin.Network.RetrofitClientInstanceService;
import com.barbera.barberahomesalon.Admin.util.FilePath;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pubnub.kaushik.realtimetaxiandroiddemo.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    private CheckBox trend_true,trend_false;
    private EditText type;
    private EditText subtype;
    private EditText name;
    private TextView nameT,imagePath;
    private Button save,imageChooser;
    private String name1,id,token,encodedImage,mime;
    private Uri filePath;
    private Bitmap bitmap;
    private File file;
    private int flag=0;

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
        trend_false=findViewById(R.id.trending_false);
        trend_true=findViewById(R.id.trending_true);
        subtype=findViewById(R.id.edit_subtype);
        imageChooser=findViewById(R.id.image_chooser);
        imagePath=findViewById(R.id.image_url);
        flag=0;

        requestStoragePermission();
        imageChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        SharedPreferences preferences=getSharedPreferences("Token",MODE_PRIVATE);
        token=preferences.getString("token","no");
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
            String subtype1=intent.getStringExtra("subtype");
            String discount1=intent.getStringExtra("discount");
            boolean dod1=intent.getBooleanExtra("dod",false);
            boolean trend=intent.getBooleanExtra("trend",false);
            price.setText(price1);
            details.setText(details1);
            gender.setText(gender1);
            time.setText(time1);
            discount.setText(discount1);
            type.setText(type1);
            subtype.setText(subtype1);
            if(dod1){
                dod_true.setChecked(true);
                dod_false.setChecked(false);
            }
            else{
                dod_true.setChecked(false);
                dod_false.setChecked(true);
            }
            if(trend){
                trend_true.setChecked(true);
                trend_false.setChecked(false);
            }
            else{
                trend_true.setChecked(false);
                trend_false.setChecked(true);
            }
        }
        else{
            name.setVisibility(View.VISIBLE);
            nameT.setVisibility(View.VISIBLE);
        }
        trend_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trend_false.setChecked(false);
                trend_true.setChecked(true);
            }
        });
        trend_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trend_false.setChecked(true);
                trend_true.setChecked(false);
            }
        });
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

                if(!dod_true.isChecked() && !dod_false.isChecked()){
                    Toast.makeText(getApplicationContext(),"Please select deal of the day true or false",Toast.LENGTH_SHORT).show();
                }
                else if(!trend_true.isChecked() && !trend_false.isChecked()){
                    Toast.makeText(getApplicationContext(),"Please select trending true or false",Toast.LENGTH_SHORT).show();
                }
                else if(!gender.getText().toString().equals("male") && !gender.getText().toString().equals("female") && !gender.getText().toString().equals("none")){
                    gender.setError("Enter male or female or none");
                }
                else if(price.getText().toString().isEmpty()){
                    price.setError("Enter price");
                }
                else if(discount.getText().toString().isEmpty()){
                    price.setError("Enter cut price");
                }
                else if(time.getText().toString().isEmpty()){
                    price.setError("Enter time");
                }
                else if(type.getText().toString().isEmpty()){
                    price.setError("Enter type of service");
                }
//                else if(det.isEmpty()){
//                    price.setError("Enter details");
//                }

                else{
                    int pr=Integer.parseInt(price.getText().toString());
                    String sub=subtype.getText().toString();
                    String det=details.getText().toString();
                    int tm=Integer.parseInt(time.getText().toString());
                    String ty=type.getText().toString();
                    int dis=Integer.parseInt(discount.getText().toString());
                    String gen=gender.getText().toString();
                    String nm=name.getText().toString();
                    boolean dod,trending;
                    if(dod_true.isChecked()){
                        dod=true;
                    }
                    else{
                        dod=false;
                    }
                    if(trend_true.isChecked()){
                        trending=true;
                    }
                    else{
                        trending=false;
                    }
                    Retrofit retrofit= RetrofitClientInstanceService.getRetrofitInstance();
                    JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
                    if(add.equals("yes")){
                        ProgressDialog progressDialog = new ProgressDialog(EditService.this);
                        progressDialog.setMessage("Hold on for a moment...");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        MultipartBody.Part img;
//                        if(flag==1){
//                            File file= new File(filePath.getPath());
//
//                        }
//                        else{
//                            img=null;
//                        }
//                        RequestBody nam=RequestBody.create(MediaType.parse("multipart/form-data"),name.getText().toString());
//                        RequestBody pri=RequestBody.create(MediaType.parse("multipart/form-data"),price.getText().toString());
//                        RequestBody tim=RequestBody.create(MediaType.parse("multipart/form-data"),time.getText().toString());
//                        RequestBody deta=RequestBody.create(MediaType.parse("multipart/form-data"),details.getText().toString());
//                        RequestBody disc=RequestBody.create(MediaType.parse("multipart/form-data"),discount.getText().toString());
//                        RequestBody ge=RequestBody.create(MediaType.parse("multipart/form-data"),gender.getText().toString());
//                        RequestBody typ=RequestBody.create(MediaType.parse("multipart/form-data"),type.getText().toString());
//                        RequestBody subt=RequestBody.create(MediaType.parse("multipart/form-data"),subtype.getText().toString());
//                        RequestBody dodd=RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(dod));
//                        RequestBody tr=RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(trending));

                        String path= FilePath.getPath(getApplicationContext(),filePath);
                        file=new File(path);
                        RequestBody requestBody=RequestBody.create(MediaType.parse("image/*"),file);
                        img=MultipartBody.Part.createFormData("image",file.getName(),requestBody);
//                        ObjectMapper objectMapper=new ObjectMapper();
//                        StringBuilder data=new StringBuilder();
//                        data.append(objectMapper.writeValueAsString());
//                        ,nam,pri,tim,disc,ge,deta,typ,subt,dodd,tr,null,null
                        Call<Void> call=jsonPlaceHolderApi.addService(new Service(nm,pr,tm,det,dis,gen,ty,dod,null,trending,sub,encodedImage),"Bearer "+token);
//                        Call<Void> call=jsonPlaceHolderApi.addService(new Service(nm,pr,tm,det,dis,gen,ty,dod,null,trending,sub,encodedImage),"Bearer "+token);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
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
                            public void onFailure(Call<Void> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(EditService.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                                Toast.makeText(EditService.this,"On failure",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        ProgressDialog progressDialog = new ProgressDialog(EditService.this);
                        progressDialog.setMessage("Hold on for a moment...");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        Call<Service> call=jsonPlaceHolderApi.updateService(new Service(name1,pr,tm,det,dis,gen,ty,dod,id,trending,sub,bitmap.toString()),"Bearer "+token);
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
                                    intent1.putExtra("subtype",sub);
                                    intent1.putExtra("gender",gen);
                                    intent1.putExtra("dod",dod);
                                    intent1.putExtra("discount",dis);
                                    intent1.putExtra("name",name1);
                                    intent1.putExtra("id",id);
                                    intent1.putExtra("trend",trending);
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
    private void requestStoragePermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            return;
        }
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},211);
    }
    private void showFileChooser(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 311);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==311 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            filePath = data.getData();
            String path= FilePath.getPath(getApplicationContext(),filePath);
            file=new File(path);

            byte[] bytes = new byte[(int)file.length()];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
                buf.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                encodedImage= Base64.getEncoder().encodeToString(bytes);
            }
//            Toast.makeText(getApplicationContext(),encodedImage,Toast.LENGTH_SHORT).show();
            //imagePath.setText(encodedImage);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==211){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Permission not granted",Toast.LENGTH_SHORT).show();
            }
        }
    }
}