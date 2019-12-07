package com.example.harmit.swooshcar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button login;
    TextView forget_pwd, sign_up;
    String PasswordHolder, UserNameHolder;
    ProgressDialog progressDialog;

    String HttpURL = "https://lardier-dawns.000webhostapp.com/login.php";
    String fullname = "";

    private static final int PERMISSION_REQUEST_CODE = 200;
    View view;

    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String finalResult;

    SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar a = getSupportActionBar();
        a.hide();

        sharedPreferences = getSharedPreferences("login_data", 0);
        editor = sharedPreferences.edit();

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);

        login.setEnabled(false);
        login.setBackground(getApplication().getResources().getDrawable(R.drawable.btn_login));
        login.setTextColor(getApplication().getResources().getColor(R.color.text_normal));

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    login.setEnabled(false);

                } else {

                    password.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            if (charSequence.toString().equals("")) {
                                login.setEnabled(false);
                            } else {
                                login.setEnabled(true);
                                login.setBackgroundColor(getApplication().getResources().getColor(R.color.btn));
                                login.setTextColor(getApplication().getResources().getColor(R.color.White));
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        forget_pwd = (TextView) findViewById(R.id.forget_pwd);
        forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forget_pwd = new Intent(LoginActivity.this, ForgetPwdActivity.class);
                startActivity(forget_pwd);
            }
        });

        sign_up = (TextView) findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_up = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(sign_up);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserNameHolder = username.getText().toString();
                PasswordHolder = password.getText().toString();

                if (checkPermission()) {
                    UserLoginClass userLoginClass = new UserLoginClass();
                    userLoginClass.execute(UserNameHolder, PasswordHolder);
                }
                    else {
                    requestPermission();
                }

                }
        });

    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)
                    {
                        UserLoginClass userLoginClass = new UserLoginClass();
                        userLoginClass.execute(UserNameHolder, PasswordHolder);
                }
                        else {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    public class UserLoginClass extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LoginActivity.this, "Please Wait", "Logging in", true, true);
        }

        @Override
        protected void onPostExecute(String httpResponseMsg) {

            super.onPostExecute(httpResponseMsg);

            if (httpResponseMsg.equalsIgnoreCase("Account Verified First Time Login")) {
                progressDialog.dismiss();
                editor.putString("username", UserNameHolder);
                editor.putString("password", PasswordHolder);
                editor.commit();

                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, FirstTimeLogin1Activity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            } else if (httpResponseMsg.equalsIgnoreCase("Account Verified Data Matched")) {

                Geturl url1 = new Geturl();
                url1.execute(UserNameHolder);

                editor.putString("username", UserNameHolder);
                editor.putString("password", PasswordHolder);
                editor.putString("fullname",fullname);
                editor.commit();

            } else {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected String doInBackground(String... params) {

            hashMap.put("email", params[0]);

            hashMap.put("password", params[1]);

            finalResult = httpParse.postRequest(hashMap, HttpURL);

            return finalResult;
        }
    }


    class Geturl extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String image_url = "";
            try {
                URL url = new URL("http://lardier-dawns.000webhostapp.com/swooshcar/geturl.php?id=" + params[0]);
                URLConnection urlConnection = url.openConnection();
                InputStream ins = urlConnection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(ins);
                int i = 0;
                while ((i = bis.read()) != -1) {
                    image_url = image_url + (char) i;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            image_url = image_url.trim();
            return image_url;

        }

        @Override
        protected void onPostExecute(String httpResponseMsg) {
            super.onPostExecute(httpResponseMsg);

            GetImage image = new GetImage();
            image.execute(httpResponseMsg);

        }
    }

    class GetImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap image = null;
            try {
                URL url1 = new URL(params[0]);
                image = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            super.onPostExecute(b);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            byte[] image = bytes.toByteArray();
            File folder = new File(Environment.getExternalStorageDirectory() + "/.swooshcar/" + username + "");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file_image = new File(folder, "profile.jpeg");
            try {
                OutputStream os = new FileOutputStream(file_image);
                os.write(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            GetFullName fullName = new GetFullName();
            fullName.execute(UserNameHolder);

        }
    }

    class GetFullName extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("http://lardier-dawns.000webhostapp.com/fullname.php?username=" + params[0]);
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
            editor.putString("username", UserNameHolder);
            editor.putString("password", PasswordHolder);
            editor.putString("fullname",fullname);
            editor.commit();

            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();

        }
    }
}