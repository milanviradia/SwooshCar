package com.example.harmit.swooshcar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class OfferRide2Activity extends AppCompatActivity {

    Spinner spseats, spluggage;

    ArrayList<String> arlist_seat, arlist_luggage;
    ArrayAdapter<String> adapter, adapter1;
    StringBuilder date,date1, time;
    int year, month, day, hour, minute;
    boolean aa;
    EditText cmnts;
    Button tp, datepicker;
    TextView tvprice;
    Button offerride1;
    String finalResult;
    String[] distan;
    String pickup_point;
    String destination_point;
    String duration;
    String distance;
    String journey_date;
    String journey_time;
    String seats;
    int price;
    String price2;
    String luggage;
    String comments;
    String HttpURL = "https://lardier-dawns.000webhostapp.com/offer_ride.php";

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    String username;

    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_ride2);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("login_data",0);
        editor = sharedPreferences.edit();

        Intent i = getIntent();
        Bundle b = i.getExtras();
        pickup_point = b.getString("origin");
        destination_point = b.getString("destination");
        duration = b.getString("duration");
        distance = b.getString("distance");

        username = sharedPreferences.getString("username","");

        tvprice = (TextView) findViewById(R.id.price);

        price = Integer.parseInt(distance) * 3;
        tvprice.setText(price+"");

        datepicker = (Button) findViewById(R.id.datePicker);
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDate = new DatePickerDialog(OfferRide2Activity.this, myDateListener, 2016, 2, 24);
                mDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mDate.show();
            }
        });

        tp = (Button) findViewById(R.id.timepicker);

        spseats = (Spinner) findViewById(R.id.seats);
        arlist_seat = new ArrayList<String>();
        arlist_seat.add("1");
        arlist_seat.add("2");
        arlist_seat.add("3");
        arlist_seat.add("4");

        adapter = new ArrayAdapter<String>(OfferRide2Activity.this, R.layout.support_simple_spinner_dropdown_item, arlist_seat);
        spseats.setAdapter(adapter);
        spseats.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seats = arlist_seat.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spluggage = (Spinner) findViewById(R.id.luggage);
        arlist_luggage = new ArrayList<String>();
        arlist_luggage.add("No Luggage");
        arlist_luggage.add("Medium Luggage");
        arlist_luggage.add("Heavy Luggage");

        adapter1 = new ArrayAdapter<String>(OfferRide2Activity.this, R.layout.support_simple_spinner_dropdown_item, arlist_luggage);
        spluggage.setAdapter(adapter1);
        spluggage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                luggage = arlist_luggage.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        offerride1 = (Button) findViewById(R.id.offerride1);
        offerride1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                price = Integer.parseInt(tvprice.getText().toString());
                price2 = String.valueOf(price);
                String[] pickup=pickup_point.split(",");
                String[] des=destination_point.split(",");


                cmnts = (EditText) findViewById(R.id.edcmnts);
                comments = cmnts.getText().toString();

//                Toast.makeText(OfferRide2Activity.this, username+pickup_point+destination_point+duration+distance, Toast.LENGTH_LONG).show();
//                Toast.makeText(OfferRide2Activity.this, journey_date+journey_time+seats+price, Toast.LENGTH_LONG).show();
//                Toast.makeText(OfferRide2Activity.this, comments, Toast.LENGTH_LONG).show();

                OfferRideFunctionClass offerRideFunctionClass = new OfferRideFunctionClass();
                offerRideFunctionClass.execute(username,pickup[0], des[0], duration, distance, journey_date, journey_time, seats, price2, luggage, comments);
            }
        });

    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(1);
        Toast.makeText(getApplicationContext(), "select Date", Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("deprecation")
    public void setTime(View view) {
        showDialog(2);
        Toast.makeText(getApplicationContext(), "Select Time", Toast.LENGTH_SHORT).show();
    }

    @Override
    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            return new DatePickerDialog(this, myDateListener, year, month, day);

        }
        if (id == 2) {
            return new TimePickerDialog(this, myTimeListener, hour, minute, aa);
        }

        return null;
    }

    final DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int Year, int monthOfYear, int dayOfMonth) {
            year = Year;
            month = monthOfYear + 1;
            day = dayOfMonth;
            view.setMinDate(System.currentTimeMillis() - 1000);
            date = new StringBuilder().append(year).append("-").append(month).append("-").append(day);
            journey_date = date.toString();

            date1 = new StringBuilder().append(day).append("-").append(month).append("-").append(year);
            datepicker.setText(date1.toString());
        }
    };

    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
            hour = arg1;

            minute = arg2;
            time = new StringBuilder().append(hour).append(":").append(minute).append(":").append("00");
            journey_time = time.toString();
            tp.setText(time);

        }
    };



    public void increment(View view) {

        price = price + 10;
        display(price);

    }

    private void display(int p)

    {
        tvprice.setText(p+"");
    }

    public void decrement(View view) {

        if (price == 0) {
            display(price);
        } else {
            price = price - 10;
            display(price);
        }
    }


    class OfferRideFunctionClass extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(OfferRide2Activity.this, "Please Wait", "Posting Your Ride", true, true);
        }

        @Override
        protected void onPostExecute(String httpResponseMsg) {

            super.onPostExecute(httpResponseMsg);
            progressDialog.dismiss();

            Toast.makeText(OfferRide2Activity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(OfferRide2Activity.this, MainActivity.class);
            startActivity(intent);
            OfferRide2Activity.this.finish();

        }

        @Override
        protected String doInBackground(String... params) {

            hashMap.put("user_name", params[0]);

            hashMap.put("pickup_point", params[1]);

            hashMap.put("destination_point", params[2]);

            hashMap.put("duration", params[3]);

            hashMap.put("distance", params[4]);

            hashMap.put("date", params[5]);

            hashMap.put("time", params[6]);

            hashMap.put("seats_offered", params[7]);

            hashMap.put("price", params[8]);

            hashMap.put("luggage_size", params[9]);

            hashMap.put("comments", params[10]);

            finalResult = httpParse.postRequest(hashMap, HttpURL);

            return finalResult;
        }

    }

}

