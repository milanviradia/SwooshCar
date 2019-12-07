package com.example.harmit.swooshcar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class RideFragment extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String username;
    File folder_car;

    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sharedPreferences = getActivity().getSharedPreferences("login_data", 0);
        editor = sharedPreferences.edit();

        username = sharedPreferences.getString("username", "");

        View view = inflater.inflate(R.layout.activity_ride_fragment, container, false);

        Button offer_ride = (Button) view.findViewById(R.id.offer_ride);
        offer_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                folder_car = new File(Environment.getExternalStorageDirectory() + "/.swooshcar/" + username + "/car.jpeg");

                if (folder_car.exists()) {
                    Intent button = new Intent(getActivity(), OfferRide1.class);
                    startActivity(button);

                } else {
                    CarCheckClass carCheckClass = new CarCheckClass();
                    carCheckClass.execute(username);
                }


            }
        });

        Button find_ride = (Button) view.findViewById(R.id.find_ride);
        find_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent button = new Intent(getActivity(), FindRide1.class);
                startActivity(button);
            }
        });

        return view;
    }

    class CarCheckClass extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String httpResponseMsg) {

            super.onPostExecute(httpResponseMsg);

            if (httpResponseMsg.equalsIgnoreCase("yes")) {
                Intent intent = new Intent(getActivity().getApplication(), OfferRide1.class);
                startActivity(intent);
            } else if (httpResponseMsg.equalsIgnoreCase("no")) {
                Toast.makeText(getActivity().getApplication(), "Upload Car Details", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity().getApplication(), CarRegistrationActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity().getApplication(), httpResponseMsg, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected String doInBackground(String... params) {

            String s = "";

            try {
                URL url = new URL("https://lardier-dawns.000webhostapp.com/swooshcar/car_check.php?username=" + params[0]);

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

