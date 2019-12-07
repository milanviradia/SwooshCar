package com.example.harmit.swooshcar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FindRide2Activity extends AppCompatActivity {

    List<GetDataAdapter> GetDataAdapter = new ArrayList<GetDataAdapter>();

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerViewAdapter recyclerViewadapter;
    Button button;

    ProgressBar progressBar;
    String pickup_point1, destination_point1, duration1, distance1;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ride2);

        sharedPreferences = getSharedPreferences("login_data", 0);
        editor = sharedPreferences.edit();
        username = sharedPreferences.getString("username", "");

        GetDataAdapter = new ArrayList<>();
        Intent i = getIntent();
        Bundle b = i.getExtras();
        pickup_point1 = b.getString("origin1");
        destination_point1 = b.getString("destination1");
        duration1 = b.getString("duration1");
        distance1 = b.getString("distance1");

        String[] pickup = pickup_point1.split(",");
        String[] des = destination_point1.split(",");

        FindRideClass findRideClass = new FindRideClass();
        findRideClass.execute(username, pickup[0], des[0]);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        recyclerView.setHasFixedSize(true);
        recyclerViewadapter = new RecyclerViewAdapter(GetDataAdapter, this);

        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        recyclerView.setAdapter(recyclerViewadapter);

    }

    class FindRideClass extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String httpResponseMsg) {

            super.onPostExecute(httpResponseMsg);
            try {
                JSONArray ja = new JSONArray(httpResponseMsg);
                int l = ja.length();
                for (int i = 0; i < l; i++) {

                    JSONObject jo = (JSONObject) ja.get(i);
                    if ((Integer.parseInt(jo.getString("seats_offered")) > 0) && (jo.getString("driver_name").compareTo(username) != 0)) {
                        String rideid = jo.getString("ride_id");
                        String drivername = jo.getString("driver_name");
                        String pickup = jo.getString("pickup_point");
                        String destination = jo.getString("destination_point");
                        String departuredate = jo.getString("date");
                        String departuretime = jo.getString("time");
                        String paymenttype = jo.getString("payment_method");
                        String seats = jo.getString("seats_offered");
                        String distance = jo.getString("distance");
                        String duration = jo.getString("duration");
                        String price = jo.getString("price");
                        String profileimage = jo.getString("profile_image");

                        GetDataAdapter ga = new GetDataAdapter(rideid, drivername, pickup, destination, departuredate, departuretime, seats, paymenttype, distance, duration, price, profileimage);
                        GetDataAdapter.add(ga);

                        recyclerViewadapter.notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String s = "";

            try {
                URL url = new URL("https://lardier-dawns.000webhostapp.com/find_ride.php?username=" + params[0] + "&pickup_point=" + params[1] + "&destination_point=" + params[2]);

                URLConnection con = url.openConnection();
                InputStream is = con.getInputStream();
                BufferedInputStream bf = new BufferedInputStream(is);
                int i;
                while ((i = bf.read()) != -1) {
                    s = s + (char) i;
                }
            } catch (Exception e) {

            }
            return s;
        }
    }
}
