package com.example.harmit.swooshcar;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class TimelineFragment extends Fragment {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    String username;

    List<GetDataAdapter1> GetDataAdapter1 = new ArrayList<GetDataAdapter1>();

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerViewAdapter1 recyclerViewadapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        sharedPreferences = getActivity().getSharedPreferences("login_data", 0);
        editor = sharedPreferences.edit();

        View v = inflater.inflate(R.layout.activity_timeline_fragment, container, false);

        username = sharedPreferences.getString("username", "");
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView2);

        recyclerView.setHasFixedSize(true);
        recyclerViewadapter = new RecyclerViewAdapter1(GetDataAdapter1, getActivity());

        recyclerViewlayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        recyclerView.setAdapter(recyclerViewadapter);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TimelineClass timeLineClass = new TimelineClass();
        timeLineClass.execute(username);
        }

    class TimelineClass extends AsyncTask<String, Void, String> {

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
                    String rideid = jo.getString("ride_id");
                    String drivername = jo.getString("driver_name");
                    String driver_image = jo.getString("driver_image");
                    String pickup = jo.getString("pickup_Point");
                    String destination = jo.getString("destination_Point");
                    String price = jo.getString("price");
                    String departuredate = jo.getString("date");
                    String departuretime = jo.getString("time");

                    GetDataAdapter1 ga = new GetDataAdapter1(rideid, drivername,driver_image, pickup, destination, price, departuredate, departuretime);
                    GetDataAdapter1.add(ga);

                    recyclerViewadapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
            }

        }

        @Override
        protected String doInBackground(String... params) {

            String s = "";

            try {
                URL url = new URL("https://lardier-dawns.000webhostapp.com/booked_ride.php?username=" + params[0]);

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

