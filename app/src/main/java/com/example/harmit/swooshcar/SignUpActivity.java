package com.example.harmit.swooshcar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    Button sign_up;
    EditText username, email, password, repassword;
    TextView moveTologin;

    String UNameHolder, EmailHolder, PasswordHolder, RePasswordHolder;
    String finalResult;
    String HttpURL = "https://lardier-dawns.000webhostapp.com/registration.php";
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar a = getSupportActionBar();
        a.hide();

        sign_up = (Button) findViewById(R.id.sign_up);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);

        sign_up.setEnabled(false);
        sign_up.setBackground(getApplication().getResources().getDrawable(R.drawable.btn_login));
        sign_up.setTextColor(getApplication().getResources().getColor(R.color.text_normal));

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().equals("")) {
                    sign_up.setEnabled(false);
                } else {
                    email.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (charSequence.toString().equals("")) {
                                sign_up.setEnabled(false);

                            } else {

                                password.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                        if (charSequence.toString().equals("")) {
                                            sign_up.setEnabled(false);
                                        } else {


                                            repassword.addTextChangedListener(new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                    if (charSequence.toString().equals("")) {
                                                        sign_up.setEnabled(false);
                                                    } else {
                                                        sign_up.setEnabled(true);
                                                        sign_up.setBackgroundColor(getApplication().getResources().getColor(R.color.btn));
                                                        sign_up.setTextColor(getApplication().getResources().getColor(R.color.White));
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
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckValidation();

                if (isValidUsername(UNameHolder) && isValidEmail(EmailHolder) && isValidPassword(PasswordHolder) && isPasswordMatched(PasswordHolder, RePasswordHolder)) {

                    // If All the value will be validate then call

                    UserRegisterFunction(UNameHolder, EmailHolder, PasswordHolder);

                } else {

                    // If Any error then this block will execute .
                    Toast.makeText(SignUpActivity.this, "Please fill correct details.", Toast.LENGTH_LONG).show();

                }

            }
        });


        moveTologin = (TextView) findViewById(R.id.moveToLogin);
        moveTologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveTologin = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(moveTologin);
            }
        });


    }

    public void CheckValidation() {
        UNameHolder = username.getText().toString();
        EmailHolder = email.getText().toString();
        PasswordHolder = password.getText().toString();
        RePasswordHolder = repassword.getText().toString();


        if (!isValidUsername(UNameHolder)) {
            username.setError("Username should contain 3 to15 characters.");
        }

        if (!isValidEmail(EmailHolder)) {
            email.setError("Enter valid email address.");
        }

        if (!isValidPassword(PasswordHolder)) {
            password.setError("Password should contain atleast 8 characters.");
        }

        if (!isPasswordMatched(PasswordHolder, RePasswordHolder)) {
            repassword.setError("Password doesn't match");
        }

    }

    // validating username
    private boolean isValidUsername(String uname) {
        String USERNAME_PATTERN = "^[A-Za-z0-9_-]{3,15}$";

        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(uname);
        return matcher.matches();
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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

    public void UserRegisterFunction(final String uname, final String email, final String password) {

        class UserRegisterFunctionClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(SignUpActivity.this, "Please Wait", "Registering", true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();

                Toast.makeText(SignUpActivity.this, httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                SignUpActivity.this.finish();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("uname", params[0]);

                hashMap.put("email", params[1]);

                hashMap.put("password", params[2]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();

        userRegisterFunctionClass.execute(uname, email, password);
    }
}
