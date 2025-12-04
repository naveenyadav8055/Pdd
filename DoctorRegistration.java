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
import android.widget.CheckBox;
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

public class DoctorRegistration extends AppCompatActivity {

    private EditText edtDoctorName, edtSpecialist, edtDoctorAge, edtExperience, edtMobileNumber, edtPassword;
    private Button btnRegister;
    private TextView tvLoginLink;
    private CheckBox chkTerms;

    // ✅ Use the correct IP based on your setup (Real Device vs Emulator)
    private static final String REGISTER_URL = "http://14.139.187.229:8081/jan2025/autism/doctordetails.php"; // Change IP if needed
    //private static final String REGISTER_URL = "http://192.168.1.4/API/doctordetails.php"; // Use for Room wifi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_registration);

        // Initialize UI elements
        edtDoctorName = findViewById(R.id.et_doctor_name);
        edtSpecialist = findViewById(R.id.et_specialist);
        edtDoctorAge = findViewById(R.id.et_doctor_age);
        edtExperience = findViewById(R.id.et_experience);
        edtMobileNumber = findViewById(R.id.et_mobile_number);
        edtPassword = findViewById(R.id.et_password);
        chkTerms = findViewById(R.id.cb_terms);
        btnRegister = findViewById(R.id.btn_register);
        tvLoginLink = findViewById(R.id.tv_login_link);

        btnRegister.setOnClickListener(v -> registerDoctor());

        tvLoginLink.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorRegistration.this, DoctorLogin.class);
            startActivity(intent);
        });
    }

    private void registerDoctor() {
        if (!isConnected()) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        String doctorName = edtDoctorName.getText().toString().trim();
        String specialist = edtSpecialist.getText().toString().trim();
        String doctorAge = edtDoctorAge.getText().toString().trim();
        String experience = edtExperience.getText().toString().trim();
        String mobileNumber = edtMobileNumber.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (doctorName.isEmpty() || specialist.isEmpty() || doctorAge.isEmpty() ||
                experience.isEmpty() || mobileNumber.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mobileNumber.matches("\\d{10,15}")) {
            Toast.makeText(this, "Enter a valid mobile number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!chkTerms.isChecked()) {
            Toast.makeText(this, "Accept terms to continue", Toast.LENGTH_SHORT).show();
            return;
        }

        new RegisterDoctorTask().execute(doctorName, specialist, doctorAge, experience, mobileNumber, password);
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private class RegisterDoctorTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(DoctorRegistration.this, "Registering", "Please wait...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(REGISTER_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                HashMap<String, String> postData = new HashMap<>();
                postData.put("doctorname", params[0]);
                postData.put("specialist", params[1]);
                postData.put("doctorage", params[2]);
                postData.put("experience", params[3]);
                postData.put("mobilenumber", params[4]);
                postData.put("password", params[5]);

                Writer writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                writer.write(getPostDataString(postData));
                writer.close();

                // Read response from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return response.toString();

            } catch (Exception e) {
                Log.e("DoctorRegistrationError", "Error: " + e.getMessage(), e);
                return "{\"status\":\"error\",\"message\":\"Connection failed: " + e.getMessage() + "\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            Log.d("DoctorRegistration", result);

            try {
                JSONObject response = new JSONObject(result);
                String status = response.getString("status");
                String message = response.getString("message");

                if (status.equalsIgnoreCase("success")) {
                    Toast.makeText(DoctorRegistration.this, message, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DoctorRegistration.this, DoctorLogin.class));
                    finish();
                } else {
                    Toast.makeText(DoctorRegistration.this, "Registration Failed: " + message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(DoctorRegistration.this, "Unexpected error occurred", Toast.LENGTH_LONG).show();
                Log.e("DoctorRegistrationError", "Parsing Error: ", e);
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
