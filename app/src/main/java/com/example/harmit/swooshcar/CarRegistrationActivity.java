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
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class CarRegistrationActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String username;

    Button SelectImageGallery, save;
    Bitmap bitmap;
    ImageView imageView;
    EditText NumberPlate, Manufacturer, Model, Colour;
    String number_plate, manufacturer, model, colour;
    ProgressDialog progressDialog;
    CheckBox Insurence, RC_Book, PUC, First_Aid_kit;

    String ConvertImage;

    String Insurence_string = "no", RC_Book_string = "no", PUC_string = "no", First_Aid_kit_string = "no";

    String HttpURL = "http://lardier-dawns.000webhostapp.com/swooshcar/car_registration.php";
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String finalResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_registration);
        NumberPlate = (EditText) findViewById(R.id.number_plate);
        Manufacturer = (EditText) findViewById(R.id.input_manufacturer);
        Model = (EditText) findViewById(R.id.input_model);
        Colour = (EditText) findViewById(R.id.input_colour);


        sharedPreferences = getSharedPreferences("login_data", 0);
        editor = sharedPreferences.edit();

        username = sharedPreferences.getString("username", "");

        Insurence = (CheckBox) findViewById(R.id.insurence);
        RC_Book = (CheckBox) findViewById(R.id.rc_book);
        PUC = (CheckBox) findViewById(R.id.puc);
        First_Aid_kit = (CheckBox) findViewById(R.id.first_aid_kit);

        imageView = (ImageView) findViewById(R.id.set_car_image);

        Insurence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Insurence.isChecked()) {
                    Insurence_string = "yes";
                    Toast.makeText(CarRegistrationActivity.this, Insurence_string, Toast.LENGTH_LONG).show();

                } else {
                    Insurence_string = "no";
                    Toast.makeText(CarRegistrationActivity.this, Insurence_string, Toast.LENGTH_LONG).show();


                }
            }
        });

        RC_Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RC_Book.isChecked()) {
                    RC_Book_string = "yes";
                } else {
                    RC_Book_string = "no";
                }
            }
        });

        PUC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PUC.isChecked()) {
                    PUC_string = "yes";
                } else {
                    PUC_string = "no";
                }
            }
        });

        First_Aid_kit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (First_Aid_kit.isChecked()) {
                    First_Aid_kit_string = "yes";
                } else {
                    First_Aid_kit_string = "no";
                }
            }
        });


        SelectImageGallery = (Button) findViewById(R.id.select_car_image);

        save = (Button) findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                number_plate = NumberPlate.getText().toString();
                manufacturer = Manufacturer.getText().toString();
                model = Model.getText().toString();
                colour = Colour.getText().toString();

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
                byte[] image = bytes.toByteArray();
                File folder = new File(Environment.getExternalStorageDirectory()+"/.swooshcar/"+username+"");
                if(!folder.exists()){
                    folder.mkdirs();
                }
                File file_image = new File(folder,"car.jpeg");
                try {
                    OutputStream os =new FileOutputStream(file_image);
                    os.write(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                CarDetailUploadFunction();

            }
        });

        SelectImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);

            }
        });

    }

    @Override
    public void onActivityResult(int RC, int RQC, Intent I) {

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


    public void CarDetailUploadFunction() {

        ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(CarRegistrationActivity.this, "Image is Uploading", "Please Wait", false, false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(CarRegistrationActivity.this, string1, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(CarRegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                CarRegistrationActivity.this.finish();


            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String, String> HashMap = new HashMap<String, String>();

                HashMap.put("username", username);

                HashMap.put("number_plate",number_plate );

                HashMap.put("manufacture",manufacturer);

                HashMap.put("model",model);

                HashMap.put("colour", colour );

                HashMap.put("insurence",Insurence_string);

                HashMap.put("rc_book", RC_Book_string);

                HashMap.put("puc", PUC_string);

                HashMap.put("first_aid_kit",First_Aid_kit_string);

                HashMap.put("car_image", ConvertImage);

                String FinalData = imageProcessClass.imageHttpRequest(HttpURL, HashMap);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

}


