package com.example.harmit.swooshcar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BookedRideDetail extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    TextView Pickup,Destination,JourneyDate,JourneyTime,DriverName,Price;
    Button Back;

    String fullname,fullname1;
    String username,rdpickup,rddestination,rddate,rdtime,rddrivername,rdrideid,rdpaymenttype,rddistance,rdprice;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_ride_detail);

        Pickup = findViewById(R.id.brdpickup1);
        Destination = findViewById(R.id.brddestination1);
        JourneyDate = findViewById(R.id.brddate1);
        JourneyTime = findViewById(R.id.brdtime1);
        DriverName = findViewById(R.id.brddriver1);
        Price = findViewById(R.id.brdprice1);
        Back = findViewById(R.id.brdback);

        sharedPreferences = getSharedPreferences("login_data",0);
        editor = sharedPreferences.edit();
        username = sharedPreferences.getString("username","");
        fullname = sharedPreferences.getString("fullname", "");
        fullname1 = sharedPreferences.getString("fullname1", "");


        Intent i = getIntent();
        Bundle b = i.getExtras();
        rdpickup = b.getString("brpickup");
        rddestination = b.getString("brdestination");
        rddate = b.getString("brdate");
        rdtime = b.getString("brtime");
        rddrivername = b.getString("brdrivername");
        rdrideid = b.getString("brrideid");
        rdprice = b.getString("brprice");

        Pickup.setText(rdpickup);
        Destination.setText(rddestination);
        JourneyDate.setText(rddate);
        JourneyTime.setText(rdtime);
        DriverName.setText(rddrivername);
        Price.setText(rdprice);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });
    }
}
