package com.example.harmit.swooshcar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class Verification extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String username;

    ImageView aadhar_verified_image_right, aadhar_image,aadhar_verified_image_wrong,licance_verified_image_right,licance_verified_image_wrong, licance_image;
    Button aadhar_upload, licance_upload;
    TextView aadhar_status_text, licance_status_text;

    File folder_aadhar,folder_licance;
    Bitmap bitmap,bitmap1;
    ProgressDialog progressDialog;
    String AadharServerUploadPath ="https://lardier-dawns.000webhostapp.com/swooshcar/aadhar_upload.php";
    String LicanceServerUploadPath ="https://lardier-dawns.000webhostapp.com/swooshcar/licance_upload.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        sharedPreferences = getSharedPreferences("login_data",0);
        editor = sharedPreferences.edit();
        username = sharedPreferences.getString("username","");

        aadhar_verified_image_right = (ImageView) findViewById(R.id.aadhar_verified_image_right);
        aadhar_verified_image_wrong = (ImageView) findViewById(R.id.aadhar_verified_image_wrong);

        aadhar_image = (ImageView) findViewById(R.id.aadhar_image);

        aadhar_status_text = (TextView) findViewById(R.id.aadhar_states_text);
        aadhar_verified_image_right.setVisibility(View.GONE);
        aadhar_upload = (Button) findViewById(R.id.select_aadhar);

        licance_verified_image_right = (ImageView) findViewById(R.id.licance_verified_image_right);
        licance_verified_image_wrong = (ImageView) findViewById(R.id.licance_verified_image_wrong);

        licance_image = (ImageView) findViewById(R.id.licance_image);

        licance_status_text = (TextView) findViewById(R.id.licance_states_text);
        licance_verified_image_right.setVisibility(View.GONE);
        licance_upload = (Button) findViewById(R.id.select_licance);

        folder_aadhar = new File(Environment.getExternalStorageDirectory() + "/.swooshcar/"+username+"/aadhar.jpeg");
        folder_licance = new File(Environment.getExternalStorageDirectory() + "/.swooshcar/"+username+"/licance.jpeg");

        GetVerification gf = new GetVerification();
        gf.execute(username);

        aadhar_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);

            }
        });

        licance_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 2);

            }
        });

    }


    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri1 = I.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri1);
                aadhar_image.setImageBitmap(bitmap);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                byte[] image = bytes.toByteArray();
                File folder = new File(Environment.getExternalStorageDirectory() + "/.swooshcar/"+username+"");
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                File file_image = new File(folder, "aadhar.jpeg");
                try {
                    OutputStream os = new FileOutputStream(file_image);
                    os.write(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                AadharImageUploadToServerFunction();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if (RC == 2 && RQC == RESULT_OK && I != null && I.getData() != null) {

                Uri uri2 = I.getData();

            try {

                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);
                licance_image.setImageBitmap(bitmap1);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                byte[] image = bytes.toByteArray();
                File folder = new File(Environment.getExternalStorageDirectory() + "/.swooshcar/"+username+"");
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                File file_image = new File(folder, "licance.jpeg");
                try {
                    OutputStream os = new FileOutputStream(file_image);
                    os.write(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                LicanceImageUploadToServerFunction();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    class GetVerification extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Verification.this, "Please Wait", "Loading data", true, true);

        }

        @Override
        protected String doInBackground(String... params) {
            String fullname="";
            try {
                URL url=new URL("http://lardier-dawns.000webhostapp.com/swooshcar/verification_detail.php?username="+params[0]);
                URLConnection urlConnection=url.openConnection();
                InputStream ins=urlConnection.getInputStream();
                BufferedInputStream bis=new BufferedInputStream(ins);
                int i=0;
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
        @Override
        protected void onPostExecute(String httpResponseMsg) {
            super.onPostExecute(httpResponseMsg);

            progressDialog.dismiss();

            try {
                JSONArray js = new JSONArray(httpResponseMsg);
                JSONObject jo = (JSONObject) js.get(0);
                String s1 = jo.getString("aadhar_status");
                String s2 = jo.getString("aadhar_image");
                String s3 = jo.getString("licance_status");
                String s4 = jo.getString("licance_image");

                if(s1.equalsIgnoreCase("yes")){

                    if (!folder_aadhar.exists()) {
                        GetAadhar adhar = new GetAadhar();
                        adhar.execute(s2);
                    }

                    else
                    {
                        aadhar_image.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/.swooshcar/"+username+"/aadhar.jpeg"));
                        aadhar_verified_image_right.setVisibility(View.VISIBLE);
                        aadhar_status_text.setText("Verified");
                    }


                }
                else {
                    Toast.makeText(Verification.this,"Upload your Aadhar card Detail",Toast.LENGTH_LONG).show();
                }

                if(s3.equalsIgnoreCase("yes")){

                    if (!folder_aadhar.exists()) {
                        Getlicance lic = new Getlicance();
                        lic.execute(s4);
                    }

                    else
                    {
                        licance_image.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/.swooshcar/"+username+"/licance.jpeg"));
                        licance_verified_image_right.setVisibility(View.VISIBLE);
                        licance_status_text.setText("Verified");
                    }


                }else {
                    Toast.makeText(Verification.this,"Upload your Licance Detail",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } }


    class GetAadhar extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap aadhar1 = null;
            try {
                URL url1 = new URL(params[0]);
                aadhar1 = BitmapFactory.decodeStream(url1.openConnection().getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return aadhar1;
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            super.onPostExecute(b);

            aadhar_image.setImageBitmap(b);
            aadhar_verified_image_wrong.setVisibility(View.GONE);
            aadhar_verified_image_right.setVisibility(View.VISIBLE);
            aadhar_status_text.setText("Verified");

        }
    }

    class Getlicance extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap licance1 = null;
            try {
                URL url1 = new URL(params[0]);
                licance1 = BitmapFactory.decodeStream(url1.openConnection().getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }

            return licance1;

        }

        @Override
        protected void onPostExecute(Bitmap b) {
            super.onPostExecute(b);

            licance_image.setImageBitmap(b);
            licance_verified_image_wrong.setVisibility(View.GONE);
            licance_verified_image_right.setVisibility(View.VISIBLE);
            licance_status_text.setText("Verified");

        }
    }


    public void AadharImageUploadToServerFunction(){

        ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(Verification.this,"Aadhar image Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(Verification.this,string1,Toast.LENGTH_LONG).show();
                aadhar_verified_image_wrong.setVisibility(View.GONE);
                aadhar_verified_image_right.setVisibility(View.VISIBLE);
                aadhar_status_text.setText("Verified");
            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put("username",username);

                HashMapParams.put("image_path", ConvertImage);

                String FinalData = imageProcessClass.imageHttpRequest(AadharServerUploadPath, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public void LicanceImageUploadToServerFunction(){

        ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage1 = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(Verification.this,"Licance image Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(Verification.this,string1,Toast.LENGTH_LONG).show();
                licance_verified_image_wrong.setVisibility(View.GONE);
                licance_verified_image_right.setVisibility(View.VISIBLE);
                licance_status_text.setText("Verified");

            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put("username",username);

                HashMapParams.put("image_path", ConvertImage1);

                String FinalData = imageProcessClass.imageHttpRequest(LicanceServerUploadPath, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }
}
