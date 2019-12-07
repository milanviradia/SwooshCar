package com.example.harmit.swooshcar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity {

    public static final String UserEmail = "";
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    EditText currentpassword, newpassword, renewpassword;
    Button changepasswordsubmit;
    String CurrentHolder, NewHolder, ReNewHolder;

    String finalResult;
    String HttpURL = "https://lardier-dawns.000webhostapp.com/change_password.php";
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ActionBar a = getSupportActionBar();
        a.hide();

        sharedPreferences = getSharedPreferences("login_data", 0);
        editor = sharedPreferences.edit();

        username = sharedPreferences.getString("username", "");
        password = sharedPreferences.getString("password", "");

        currentpassword = (EditText) findViewById(R.id.current_password);
        newpassword = (EditText) findViewById(R.id.new_password);
        renewpassword = (EditText) findViewById(R.id.reenter_new_password);
        changepasswordsubmit = (Button) findViewById(R.id.change_password_submit);


        changepasswordsubmit.setEnabled(false);
        changepasswordsubmit.setBackground(getApplication().getResources().getDrawable(R.drawable.btn_login));
        changepasswordsubmit.setTextColor(getApplication().getResources().getColor(R.color.text_normal));


        currentpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().equals("")) {
                    changepasswordsubmit.setEnabled(false);
                } else {
                    newpassword.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.toString().equals("")) {
                                changepasswordsubmit.setEnabled(false);

                            } else {

                                renewpassword.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                        if (charSequence.toString().equals("")) {
                                            changepasswordsubmit.setEnabled(false);
                                        } else {

                                            changepasswordsubmit.setEnabled(true);
                                            changepasswordsubmit.setBackgroundColor(getApplication().getResources().getColor(R.color.btn));
                                            changepasswordsubmit.setTextColor(getApplication().getResources().getColor(R.color.White));
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
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        changepasswordsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckValidation();

                if (isCurrentPasswordMatched(CurrentHolder, password) && isValidPassword(NewHolder) && isPasswordMatched(NewHolder, ReNewHolder)) {
                    ChangePasswordClass changePasswordClass = new ChangePasswordClass();

                    changePasswordClass.execute(username, ReNewHolder);

                } else {

                    Toast.makeText(ChangePasswordActivity.this, "Please fill all fields.", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public void CheckValidation() {
        CurrentHolder = currentpassword.getText().toString();
        NewHolder = newpassword.getText().toString();
        ReNewHolder = renewpassword.getText().toString();

        if (!isCurrentPasswordMatched(CurrentHolder, password)) {
            currentpassword.setError("Enter correct password.");
        }

        if (!isValidPassword(NewHolder)) {
            newpassword.setError("Password should contain atleast 8 characters.");
        }

        if (!isPasswordMatched(NewHolder, ReNewHolder)) {
            renewpassword.setError("Password doesn't match");
        }

    }


    private boolean isCurrentPasswordMatched(String current, String newpassword) {
        if (current.equals(newpassword)) {
            return true;
        } else return false;
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 8) {
            return true;
        } else return false;
    }

    private boolean isPasswordMatched(String pass, String repass) {
        if (pass.equals(repass)) {
            return true;
        } else return false;
    }

    class ChangePasswordClass extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ChangePasswordActivity.this, "Loading Data", null, true, true);
        }

        @Override
        protected void onPostExecute(String httpResponseMsg) {

            super.onPostExecute(httpResponseMsg);

            progressDialog.dismiss();

            if (httpResponseMsg.equalsIgnoreCase("Password Changed")) {

                Toast.makeText(ChangePasswordActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(ChangePasswordActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected String doInBackground(String... params) {

            hashMap.put("username", params[0]);

            hashMap.put("password", params[1]);

            finalResult = httpParse.postRequest(hashMap, HttpURL);

            return finalResult;
        }
    }
}