package com.example.harmit.swooshcar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class RideDetail extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    TextView Pickup, Destination, JourneyDate, JourneyTime, DriverName, PaymentType, Distance, TravelTime, Price;
    Button Confirm;
    String username, rdpickup, rddestination, rddate, rdtime, rdseats, rddrivername, rdrideid, rdpaymenttype, rddistance, rdduration, rdprice;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String finalResult;
    String HttpURL = "https://lardier-dawns.000webhostapp.com/book_ride.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail);

        Pickup = findViewById(R.id.rdpickup1);
        Destination = findViewById(R.id.rddestination1);
        JourneyDate = findViewById(R.id.rddate1);
        JourneyTime = findViewById(R.id.rdtime1);
        DriverName = findViewById(R.id.rddriver1);
        PaymentType = findViewById(R.id.rdpaymenttype1);
        Distance = findViewById(R.id.rddistance1);
        TravelTime = findViewById(R.id.rdtraveltime1);
        Price = findViewById(R.id.rdprice1);
        Confirm = findViewById(R.id.rdconfirmbtn);

        sharedPreferences = getSharedPreferences("login_data", 0);
        editor = sharedPreferences.edit();
        username = sharedPreferences.getString("username", "");

        Intent i = getIntent();
        Bundle b = i.getExtras();
        rdpickup = b.getString("rpickup");
        rddestination = b.getString("rdestination");
        rddate = b.getString("rdate");
        rdtime = b.getString("rtime");
        rdseats = b.getString("rseats");
        rddrivername = b.getString("rdrivername");
        rdrideid = b.getString("rrideid");
        rdpaymenttype = b.getString("rpaymenttype");
        rddistance = b.getString("rdistance");
        rdduration = b.getString("rduration");
        rdprice = b.getString("rprice");

        Pickup.setText(rdpickup);
        Destination.setText(rddestination);
        JourneyDate.setText(rddate);
        JourneyTime.setText(rdtime);
        DriverName.setText(rddrivername);
        PaymentType.setText(rdpaymenttype);
        Distance.setText(rddistance);
        TravelTime.setText(rdduration);
        Price.setText(rdprice);

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookRideClass bookRideClass = new BookRideClass();
                bookRideClass.execute(username, rdrideid, rdseats);
            }
        });

    }

    class BookRideClass extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(RideDetail.this, "Please Wait", "Booking Ride", true, true);

        }

        @Override
        protected void onPostExecute(String httpResponseMsg) {

            super.onPostExecute(httpResponseMsg);

            progressDialog.dismiss();

            if (httpResponseMsg.equalsIgnoreCase("Ride Booked")) {

                Toast.makeText(RideDetail.this, httpResponseMsg, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(RideDetail.this, MainActivity.class);
                startActivity(intent);
                RideDetail.this.finish();
            } else {
                Toast.makeText(RideDetail.this, httpResponseMsg, Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected String doInBackground(String... params) {

            hashMap.put("username", params[0]);

            hashMap.put("rideid", params[1]);

            hashMap.put("seats", params[2]);

            finalResult = httpParse.postRequest(hashMap, HttpURL);

            return finalResult;
        }
    }
}
