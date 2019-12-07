package com.example.harmit.swooshcar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    String s1, s2, s3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar a = getSupportActionBar();
        a.hide();

        sharedPreferences = getSharedPreferences("login_data", 0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                s1 = sharedPreferences.getString("username", "");
                s2 = sharedPreferences.getString("password", "");
                s3 = sharedPreferences.getString("fullname", "");


                if (s1.equals("") && s2.equals("")) {

                    Intent send = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(send);
                    SplashActivity.this.finish();

                } else {

                    Intent send = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(send);
                    SplashActivity.this.finish();


                }
            }
        }, 1000);
    }
}
