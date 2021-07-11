package com.barbera.barberahomesalon.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.barbera.barberahomesalon.Admin.Network.JsonPlaceHolderApi;
import com.barbera.barberahomesalon.Admin.Network.Register;
import com.barbera.barberahomesalon.Admin.Network.RetrofitClientInstanceService;
import com.barbera.barberahomesalon.Admin.Network.RetrofitClientInstanceUser;
import com.pubnub.kaushik.realtimetaxiandroiddemo.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private EditText phoneNumber;
    private CardView get_code;
    private ProgressDialog progressDialog;
    private EditText veri_code;
    private CardView continue_to_signup;
    private ProgressBar progressBar;
    private String tempToken;
    private String phonePattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneNumber = (EditText) findViewById(R.id.phone);
        get_code = (CardView) findViewById(R.id.get_code);
        veri_code = (EditText) findViewById(R.id.veri_code);
        continue_to_signup = findViewById(R.id.continue_to_signup_page);
        progressBar = findViewById(R.id.progressBarInVerificationPage);
        progressDialog = new ProgressDialog(LoginActivity.this);
        phonePattern = "^[6789]\\d{9}$";

        get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verifyPhoneNumber()) {
                    get_code.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    sendToastmsg("Sending OTP");
                    sendfVerificationCode();
                }
            }
        });
        continue_to_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyUserOTP()) {
                    continue_to_signup.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    //PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,veri_code.getText().toString());
                    Toast.makeText(getApplicationContext(), "In", Toast.LENGTH_SHORT).show();
                    verifyUser();
                }

            }
        });
    }
    private void sendToastmsg(String text) {
        Toast msg = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        msg.show();
    }
    private void verifyUser() {
        Retrofit retrofit = RetrofitClientInstanceUser.getRetrofitInstance();
        JsonPlaceHolderApi jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi.class);
        Call<Register> call = jsonPlaceHolderApi2.checkOtp(new Register(null,null, veri_code.getText().toString(),"admin",null,null,null),"Bearer "+tempToken);
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if (response.code() == 200) {
                    //sendToastmsg("Welcome");
                    Register register = response.body();
                    SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", register.getToken());
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Request not sent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean verifyUserOTP() {
        if (veri_code.getText().toString().isEmpty() || veri_code.getText().toString().length() < 6) {
            veri_code.setError("Invalid OTP");
            veri_code.requestFocus();
            return false;
        } else
            return true;
    }

    private boolean verifyPhoneNumber() {
        if (phoneNumber.getText().toString().matches(phonePattern))
            return true;
        else {
            phoneNumber.setError("Please Enter a valid Phone Number");
            phoneNumber.requestFocus();
            return false;
        }
    }

    private void sendfVerificationCode() {
        Retrofit retrofit = RetrofitClientInstanceUser.getRetrofitInstance();
        JsonPlaceHolderApi jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi.class);
        Call<Register> call = jsonPlaceHolderApi2.getToken(new Register(phoneNumber.getText().toString(), null,null, null,null,null,null));
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if (response.code() == 200) {
                    Register register = response.body();
                    tempToken = register.getToken();
                    progressBar.setVisibility(View.INVISIBLE);
                    veri_code.setVisibility(View.VISIBLE);
                    continue_to_signup.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Request not sent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences=getSharedPreferences("Token",MODE_PRIVATE);
        String isRegistered = preferences.getString("token","haha");
        if(!isRegistered.equals("no")){
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}