package com.example.harmit.swooshcar;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutYouActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String username, fullname;
    TextView FullName;
    CircleImageView ProfileImage;
    EditText FirstName, LastName, Gender, Email, MobileNumber;
    ProgressDialog progressDialog;
    File folder_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_you);

        getSupportActionBar().setTitle("About You");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("login_data", 0);
        editor = sharedPreferences.edit();

        fullname = sharedPreferences.getString("fullname", "");
        username = sharedPreferences.getString("username", "");

        ProfileImage = (CircleImageView) findViewById(R.id.profile_image);

        FullName = (TextView) findViewById(R.id.fullname);
        FullName.setText(fullname);

        FirstName = (EditText) findViewById(R.id.first_name);
        LastName = findViewById(R.id.last_name);
        Gender = findViewById(R.id.Gender);
        Email = findViewById(R.id.email);
        MobileNumber = findViewById(R.id.mobile_number);

        progressDialog = ProgressDialog.show(AboutYouActivity.this, "Please Wait", "Loading data", true, true);

        folder_profile = new File(Environment.getExternalStorageDirectory() + "/.swooshcar/" + username + "/profile.jpeg");

        GetFullName gf = new GetFullName();
        gf.execute(username);
    }


    class GetFullName extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(String... params) {
            String fullname = "";
            try {
                URL url = new URL("http://lardier-dawns.000webhostapp.com/swooshcar/about_us.php?username=" + params[0]);
                URLConnection urlConnection = url.openConnection();
                InputStream ins = urlConnection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(ins);
                int i = 0;
                while ((i = bis.read()) != -1) {
                    fullname = fullname + (char) i;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fullname = fullname.trim();
            return fullname;

        }

        @Override
        protected void onPostExecute(String httpResponseMsg) {
            super.onPostExecute(httpResponseMsg);

            try {
                JSONArray js = new JSONArray(httpResponseMsg);
                JSONObject jo = (JSONObject) js.get(0);
                String s1 = jo.getString("first_name");
                String s2 = jo.getString("last_name");
                String s3 = jo.getString("gender");
                String s4 = jo.getString("phone_number");
                String s5 = jo.getString("profile_image");

                FirstName.setText(s1);
                LastName.setText(s2);
                Gender.setText(s3);
                MobileNumber.setText(s4);

                if (!folder_profile.exists()) {
                    GetProfileimage getProfileimage = new GetProfileimage();
                    getProfileimage.execute(s5);
                } else {
                    ProfileImage.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/.swooshcar/" + username + "/profile.jpeg"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            GetEmail e = new GetEmail();
            e.execute(username);
        }


        class GetProfileimage extends AsyncTask<String, Void, Bitmap> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Bitmap doInBackground(String... params) {

                Bitmap profile1 = null;
                try {
                    URL url1 = new URL(params[0]);
                    profile1 = BitmapFactory.decodeStream(url1.openConnection().getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return profile1;
            }

            @Override
            protected void onPostExecute(Bitmap b) {
                super.onPostExecute(b);
                ProfileImage.setImageBitmap(b);
            }
        }

        class GetEmail extends AsyncTask<String, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }


            @Override
            protected String doInBackground(String... params) {
                String fullname = "";
                try {
                    URL url = new URL("https://lardier-dawns.000webhostapp.com/swooshcar/get_email.php?username=" + params[0]);
                    URLConnection urlConnection = url.openConnection();
                    InputStream ins = urlConnection.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(ins);
                    int i = 0;
                    while ((i = bis.read()) != -1) {
                        fullname = fullname + (char) i;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fullname = fullname.trim();
                return fullname;

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Email.setText(httpResponseMsg);

            }
        }
    }

}
