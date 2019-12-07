package com.example.harmit.swooshcar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DELL on 15-03-2018.
 */

public class FirstTimeLogin1Activity extends AppCompatActivity {

    EditText FirstName, LastName, PhoneNumber;
    RadioGroup Gender;
    String RadioOutput;
    Button submit_data;
    String FirstNameUpload, LastNameUpload, NumberUpload;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    String HttpURL = "https://lardier-dawns.000webhostapp.com/swooshcar/FirstTimeData.php";
    String finalResult;
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String username;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_login1);

        ActionBar a = getSupportActionBar();
        a.hide();

        sharedPreferences = getSharedPreferences("login_data", 0);
        editor = sharedPreferences.edit();

        username = sharedPreferences.getString("username", "");

        FirstName = (EditText) findViewById(R.id.first_name);
        LastName = (EditText) findViewById(R.id.last_name);
        PhoneNumber = (EditText) findViewById(R.id.phone_number);
        Gender = (RadioGroup) findViewById(R.id.gender);

        Gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = (RadioButton) findViewById(checkedId);
                RadioOutput = rb.getText().toString();

            }
        });

        submit_data = (Button) findViewById(R.id.submit_value);
        submit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckValidation();

                if (isValidFirstName(FirstNameUpload) && isValidLastName(LastNameUpload) && isValidPhoneNumber(NumberUpload)) {

                    UserDetailsFunction(username, FirstNameUpload, LastNameUpload, RadioOutput, NumberUpload);
                    editor.putString("fullname1", FirstNameUpload +" "+LastNameUpload);
                    editor.commit();
                } else {

                    // If Any error then this block will execute .
                    Toast.makeText(FirstTimeLogin1Activity.this, "Please fill correct details.", Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    public void CheckValidation() {

        FirstNameUpload = FirstName.getText().toString();
        LastNameUpload = LastName.getText().toString();
        NumberUpload = PhoneNumber.getText().toString();


        if (!isValidFirstName(FirstNameUpload)) {
            FirstName.setError("Firstname should contain 3 to 15 characters.");
        }
        if (!isValidLastName(LastNameUpload)) {
            LastName.setError("Lastname should contain 3 to 15 characters.");
        }

//        if (!isValidGender(RadioOutput)) {
//            Toast.makeText(MainActivity.this, "Please Select Gender", Toast.LENGTH_LONG).show();
//        }

        if (!isValidPhoneNumber(NumberUpload)) {
            PhoneNumber.setError("Phone number must contain 10 characters.");
        }

    }


    private boolean isValidFirstName(String firstNameUpload) {
        String FIRSTNAME_PATTERN = "^[a-zA-Z]{2,64}$";
        Pattern pattern = Pattern.compile(FIRSTNAME_PATTERN);
        Matcher matcher = pattern.matcher(firstNameUpload);
        return matcher.matches();
    }

    private boolean isValidLastName(String lastNameUpload) {
        String LASSTNAME_PATTERN = "^[A-Za-z]{2,64}$";
        Pattern pattern = Pattern.compile(LASSTNAME_PATTERN);
        Matcher matcher = pattern.matcher(lastNameUpload);
        return matcher.matches();

    }

//    private boolean isValidGender(String radioOutput) {
//        if (radioOutput != null) {
//            return true;
//        }
//        else
//            return false;
//
//    }

    private boolean isValidPhoneNumber(String numberUpload) {
        if (numberUpload != null && numberUpload.length() == 10) {
            return true;
        } else return false;

    }

    public void UserDetailsFunction(String username, final String firstname, final String lastname, final String gender, final String number) {

        class UserDetailsFunctionClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(FirstTimeLogin1Activity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if (httpResponseMsg.equalsIgnoreCase("Registration Successfully")) {

                    Intent intent = new Intent(FirstTimeLogin1Activity.this, FirstTimeLogin2Activity.class);
                    startActivity(intent);
                    FirstTimeLogin1Activity.this.finish();
                } else {
                    Toast.makeText(FirstTimeLogin1Activity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                }


            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("username", params[0]);

                hashMap.put("firstname", params[1]);

                hashMap.put("lastname", params[2]);

                hashMap.put("gender", params[3]);

                hashMap.put("phonenumber", params[4]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserDetailsFunctionClass userDetailsFunctionClass = new UserDetailsFunctionClass();

        userDetailsFunctionClass.execute(username, firstname, lastname, gender, number);

    }


}