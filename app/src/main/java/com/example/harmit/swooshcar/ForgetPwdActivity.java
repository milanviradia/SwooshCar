package com.example.harmit.swooshcar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ForgetPwdActivity extends AppCompatActivity {

    EditText email;
    Button submit;
    TextView moveTologin;

    Boolean CheckEditText;
    String EmailHolder;
    String finalResult;
    String HttpURL = "https://lardier-dawns.000webhostapp.com/forgot_password.php";
    ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    public static final String UserEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);

        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = (EditText) findViewById(R.id.forget_password_email);
        submit = (Button) findViewById(R.id.forget_password_submit);


        submit.setEnabled(false);
        submit.setBackground(getApplication().getResources().getDrawable(R.drawable.btn_login));
        submit.setTextColor(getApplication().getResources().getColor(R.color.text_normal));

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    submit.setEnabled(false);

                } else {

                    submit.setEnabled(true);
                    submit.setBackgroundColor(getApplication().getResources().getColor(R.color.btn));
                    submit.setTextColor(getApplication().getResources().getColor(R.color.White));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });


        moveTologin = (TextView) findViewById(R.id.moveTologin);
        moveTologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveTologin = new Intent(ForgetPwdActivity.this, LoginActivity.class);
                startActivity(moveTologin);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckEditTextIsEmptyOrNot();

                if (CheckEditText) {

                    ForgotPasswordFunction(EmailHolder);

                } else {

                    Toast.makeText(ForgetPwdActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public void CheckEditTextIsEmptyOrNot() {

        EmailHolder = email.getText().toString();

        CheckEditText = !(TextUtils.isEmpty(EmailHolder));
    }

    public void ForgotPasswordFunction(final String email) {

        class ForgotPasswordClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(ForgetPwdActivity.this, "Loading Data", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if (httpResponseMsg.equalsIgnoreCase("Data Matched")) {

                    finish();

                    Intent intent = new Intent(ForgetPwdActivity.this, LoginActivity.class);

                    intent.putExtra(UserEmail, email);

                    startActivity(intent);

                } else {

                    Toast.makeText(ForgetPwdActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email", params[0]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        ForgotPasswordClass forgotPasswordClass = new ForgotPasswordClass();

        forgotPasswordClass.execute(email);
    }
}
