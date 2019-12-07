package com.example.harmit.swooshcar;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class CarActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String username,car_image;
    TextInputEditText NumberPlate,Manufacture,Model,Colour;
    TextView Insurence,RC_Book,PUC,First_Aid_kit;
    ImageView Car_image;
    Bitmap image;
    ProgressDialog progressDialog;

    File folder_car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        getSupportActionBar().setTitle("Car Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NumberPlate =  findViewById(R.id.number_plate);
        Manufacture =  findViewById(R.id.input_manufacturer);
        Model =  findViewById(R.id.input_model);
        Colour = findViewById(R.id.input_colour);
        Insurence =  findViewById(R.id.insurence);
        RC_Book =  findViewById(R.id.rc_book);
        PUC =  findViewById(R.id.puc);
        First_Aid_kit =  findViewById(R.id.first_aid_kit);
        Car_image =  findViewById(R.id.set_car_image);

        sharedPreferences = getSharedPreferences("login_data",0);
        editor = sharedPreferences.edit();

        username = sharedPreferences.getString("username","");

        folder_car = new File(Environment.getExternalStorageDirectory() + "/.swooshcar/"+username+"/car.jpeg");

        GetDetail gf = new GetDetail();
        gf.execute(username);

    }

    class GetDetail extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(CarActivity.this, "Please Wait", "Loading data", true, true);

        }

        @Override
        protected void onPostExecute(String httpResponseMsg) {
            super.onPostExecute(httpResponseMsg);

            progressDialog.dismiss();

            try {
                JSONArray ja = new JSONArray(httpResponseMsg);
                int l = ja.length();
                for (int i = 0; i < l; i++) {

                    JSONObject jo = (JSONObject) ja.get(i);
                    NumberPlate.setText(jo.getString("number_plate"));
                    Manufacture.setText(jo.getString("manufacturer"));
                    Model.setText(jo.getString("model"));
                    Colour.setText(jo.getString("colour"));
                    Insurence.setText(jo.getString("insurence"));
                    RC_Book.setText(jo.getString("rc_book"));
                    PUC.setText(jo.getString("puc"));
                    First_Aid_kit.setText(jo.getString("first_aid_kit"));

                    car_image = jo.getString("car_image");

                    if (!folder_car.exists()) {
                        GetCarImage ci = new GetCarImage(Car_image);
                        ci.execute(car_image);
                    }
                    else
                    {
                        Car_image.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/.swooshcar/"+username+"/car.jpeg"));
                    }
            }
        }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String fullname="";

            try {
                URL url=new URL("http://lardier-dawns.000webhostapp.com/swooshcar/car_detail_fetch.php?username="+params[0]);
                URLConnection urlConnection=url.openConnection();
                InputStream ins=urlConnection.getInputStream();
                BufferedInputStream bis=new BufferedInputStream(ins);
                int i;
                while((i=bis.read())!=-1){
                    fullname=fullname+(char)i;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fullname=fullname.trim();
            return fullname;
        }

    }

    class GetCarImage extends AsyncTask<String, Void, Bitmap> {

        ImageView car_image;

        public GetCarImage(ImageView car_image) {
            this.car_image = car_image;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            URL url1 = null;
            try {
                url1 = new URL(params[0]);
                image = BitmapFactory.decodeStream(url1.openConnection().getInputStream());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            super.onPostExecute(b);
            car_image.setImageBitmap(b);
        }
    }
}
