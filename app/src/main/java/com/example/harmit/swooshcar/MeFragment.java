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
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class MeFragment extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Bitmap bitmap;

    ProgressDialog progressDialog;
    String UserName = "user_name";
    String ImagePath = "image_path";
    String ServerUploadPath = "https://lardier-dawns.000webhostapp.com/swooshcar/image_upload.php";

    File folder_profile;
    Button logout, AboutYou, Car, Verification, ChangePassword;
    String username,fullname,fullname1;
    TextView FullName;
    CircleImageView ProfileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sharedPreferences = getActivity().getSharedPreferences("login_data", 0);
        editor = sharedPreferences.edit();

        View v = inflater.inflate(R.layout.activity_me_fragment, container, false);
        ProfileImage = v.findViewById(R.id.profile_image);

        FullName = (TextView) v.findViewById(R.id.fullname);
        logout = (Button) v.findViewById(R.id.logout);

        AboutYou = (Button) v.findViewById(R.id.about_you);
        Car = (Button) v.findViewById(R.id.car);
        Verification = (Button) v.findViewById(R.id.verification);
        ChangePassword = (Button) v.findViewById(R.id.change_password);

        username = sharedPreferences.getString("username", "");
        fullname = sharedPreferences.getString("fullname", "");
        fullname1 = sharedPreferences.getString("fullname1", "");

        folder_profile = new File(Environment.getExternalStorageDirectory() + "/.swooshcar/" + username + "/profile.jpeg");

        if (!folder_profile.exists()) {
            GetFullName gf = new GetFullName();
            gf.execute(username);
            }
            else {
            ProfileImage.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/.swooshcar/" + username + "/profile.jpeg"));
        }

        if(fullname1.equals("")) {
            FullName.setText(fullname);
        }
        else
        {
            FullName.setText(fullname1);
        }
        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AboutYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), AboutYouActivity.class);
                startActivity(intent);
            }
        });

        Car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), CarActivity.class);
                startActivity(intent);
            }
        });

        Verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), Verification.class);
                startActivity(intent);
            }
        });

        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();

                Intent sendToLoginandRegistration = new Intent(getActivity().getApplication(), LoginActivity.class);
                startActivity(sendToLoginandRegistration);
                getActivity().finish();

            }
        });


        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                ProfileImage.setImageBitmap(bitmap);

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

                ChangeProfileImage();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    class GetFullName extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String s = "";
            try {
                URL url = new URL("http://lardier-dawns.000webhostapp.com/swooshcar/about_us.php?username=" + params[0]);
                URLConnection urlConnection = url.openConnection();
                InputStream ins = urlConnection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(ins);
                int i = 0;
                while ((i = bis.read()) != -1) {
                    s = s + (char) i;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            s = s.trim();
            return s;

        }

        @Override
        protected void onPostExecute(String httpResponseMsg) {
            super.onPostExecute(httpResponseMsg);

            try {
                JSONArray js = new JSONArray(httpResponseMsg);
                JSONObject jo = (JSONObject) js.get(0);
                String s1 = jo.getString("profile_image");

                GetProfileimage getProfileimage = new GetProfileimage();
                getProfileimage.execute(s1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
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
    }

    public void ChangeProfileImage() {

        ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(), "Image is Uploading", "Please Wait", false, false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(getActivity().getApplication(), string1, Toast.LENGTH_LONG).show();
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