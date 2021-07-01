package com.barbera.barberahomesalon.Admin;


import android.os.Bundle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.barbera.barberahomesalon.Admin.Service.ServiceActivity;
import com.barbera.barberahomesalon.Admin.util.Constants;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.kaushik.realtimetaxiandroiddemo.R;

public class MainActivity extends AppCompatActivity {

    public static PubNub pubnub; // Pubnub instance
    public static String channel = "";

    Button passengerButton,service2,service3,service4,service5,service6,service7,open,view_service; // Buttons that redirect user to proper view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        driverButton = (Button) findViewById(R.id.driverButton);
        passengerButton = (Button) findViewById(R.id.passengerButton);
        service2 = findViewById(R.id.service2);
        service3 = findViewById(R.id.service3);
        service4 = findViewById(R.id.service4);
        service5 = findViewById(R.id.service5);
        service6 = findViewById(R.id.service6);
        service7 = findViewById(R.id.service7);
        open = findViewById(R.id.open);
        view_service=findViewById(R.id.services);


        initPubnub();

        view_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServiceActivity.class));
            }
        });
        open.setOnClickListener(view -> startActivity(new Intent(this,FirebaseAdmin.class)));


        passengerButton.setOnClickListener(view ->{
            channel = "Service 1";
            sendToPassenger();
        });
        service2.setOnClickListener(view -> {
            channel = "Service 2";
            sendToPassenger();
        });
        service3.setOnClickListener(view -> {
            channel = "Service 2";
            sendToPassenger();
        });
        service4.setOnClickListener(view -> {
            channel = "Service 2";
            sendToPassenger();
        });
        service5.setOnClickListener(view -> {
            channel = "Service 2";
            sendToPassenger();
        });
        service6.setOnClickListener(view -> {
            channel = "Service 2";
            sendToPassenger();
        });
        service7.setOnClickListener(view -> {
            channel = "Service 2";
            sendToPassenger();
        });
        checkPermission();

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            checkPermission();
        }

    }

    private void sendToPassenger() {
        startActivity(new Intent(MainActivity.this, PassengerActivity.class));
    }

    /*
        Creates PNConfiguration instance and enters Pubnub credentials to create Pubnub instance.
        This Pubnub instance will be used whenever we need to create connection to Pubnub.
     */
    private void initPubnub() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(Constants.PUBNUB_SUBSCRIBE_KEY);
        pnConfiguration.setPublishKey(Constants.PUBNUB_PUBLISH_KEY);
        pnConfiguration.setSecure(true);
        pubnub = new PubNub(pnConfiguration);
    }

    /*
        Checks user's location permission to see whether user has granted access to fine location and coarse location.
        If not it will request these permissions.
     */
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }

}