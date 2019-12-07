package com.example.harmit.swooshcar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class FirstTimeLogin2Activity extends AppCompatActivity {

    Bitmap bitmap;
    Button UploadImageServer;
    ImageView imageView;
    ProgressDialog progressDialog;
    String UserName = "user_name";
    String ImagePath = "image_path";
    String ServerUploadPath = "https://lardier-dawns.000webhostapp.com/swooshcar/image_upload.php";
    String username;

    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_login2);

        ActionBar a = getSupportActionBar();
        a.hide();


        sharedPreferences = getSharedPreferences("login_data", 0);
        username = sharedPreferences.getString("username", "");

        imageView = (ImageView) findViewById(R.id.profile_image_view);

        UploadImageServer = (Button) findViewById(R.id.submit_value2);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);

            }
        });


        UploadImageServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
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

                ImageUploadToServerFunction();

            }
        });
    }

    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public void ImageUploadToServerFunction() {

        ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(FirstTimeLogin2Activity.this, "Image is Uploading", "Please Wait", false, false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(FirstTimeLogin2Activity.this, string1, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(FirstTimeLogin2Activity.this, MainActivity.class);
                startActivity(intent);
                FirstTimeLogin2Activity.this.finish();
            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String, String> HashMapParams = new HashMap<String, String>();

                HashMapParams.put(UserName, username);

                HashMapParams.put(ImagePath, ConvertImage);

                String FinalData = imageProcessClass.imageHttpRequest(ServerUploadPath, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }
}
