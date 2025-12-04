package com.SIMATS.austismapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class DoctorLogin extends AppCompatActivity {

    private EditText edtNumber, edtPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private static final String LOGIN_URL = "http://14.139.187.229:8081/jan2025/autism/doctorlogin.php"; // Change IP if needed
    //private static final String LOGIN_URL = "http://192.168.1.4/API/doctorlogin.php"; // Use Room Wifi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        edtNumber = findViewById(R.id.edtnumber);
        edtPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.login);
        tvRegister = findViewById(R.id.tv_login_link);

        btnLogin.setOnClickListener(v -> loginDoctor());

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorLogin.this, DoctorRegistration.class);
            startActivity(intent);
        });
    }

    private void loginDoctor() {
        if (!isConnected()) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        String number = edtNumber.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (number.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        new LoginDoctorTask().execute(number, password);
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private class LoginDoctorTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(DoctorLogin.this, "Logging in", "Please wait...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(LOGIN_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                HashMap<String, String> postData = new HashMap<>();
                postData.put("mobilenumber", params[0]);
                postData.put("password", params[1]);

                Writer writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                writer.write(getPostDataString(postData));
                writer.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return response.toString();

            } catch (Exception e) {
                Log.e("DoctorLoginError", "Error: " + e.getMessage(), e);
                return "{\"status\":\"error\",\"message\":\"Connection failed: " + e.getMessage() + "\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            Log.d("DoctorLogin", result);

            try {
                JSONObject response = new JSONObject(result);
                String status = response.getString("status");
                String message = response.getString("message");

                if (status.equalsIgnoreCase("success")) {
                    Toast.makeText(DoctorLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DoctorLogin.this, DoctorFirstpage.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(DoctorLogin.this, "Login Failed: " + message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(DoctorLogin.this, "Unexpected error occurred", Toast.LENGTH_LONG).show();
                Log.e("DoctorLoginError", "Parsing Error: ", e);
            }
        }
    }

    private String getPostDataString(HashMap<String, String> postData) {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : postData.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }
}