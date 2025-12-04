package com.SIMATS.austismapp;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class Registration extends AppCompatActivity {

    private EditText edtParentName, edtChildName, edtChildAge, edtChildDiagnosis, edtMobileNumber, edtPassword;
    private Button btnRegister;
    private TextView tvLoginLink;
    private CheckBox chkTerms;

    private static final String REGISTER_URL = "http://14.139.187.229:8081/jan2025/autism/details.php"; // Use local XAMPP server
    //private static final String REGISTER_URL = "http://192.168.1.4/API/details.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        // Initialize UI elements
        edtParentName = findViewById(R.id.et_parent_name);
        edtChildName = findViewById(R.id.et_child_name);
        edtChildAge = findViewById(R.id.et_child_age);
        edtChildDiagnosis = findViewById(R.id.et_child_diagnosis);
        edtMobileNumber = findViewById(R.id.et_parent_mobile);
        edtPassword = findViewById(R.id.et_password);
        chkTerms = findViewById(R.id.cb_terms);
        btnRegister = findViewById(R.id.btn_register);
        tvLoginLink = findViewById(R.id.tv_login_link);

        // Handle Register button click
        btnRegister.setOnClickListener(v -> registerUser());

        // Navigate to Login page
        tvLoginLink.setOnClickListener(v -> {
            Intent intent = new Intent(Registration.this, ParentLogin.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        String parentName = edtParentName.getText().toString().trim();
        String childName = edtChildName.getText().toString().trim();
        String childAge = edtChildAge.getText().toString().trim();
        String childDiagnosis = edtChildDiagnosis.getText().toString().trim();
        String mobileNumber = edtMobileNumber.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Validate fields
        if (parentName.isEmpty() || childName.isEmpty() ||
                childAge.isEmpty() || childDiagnosis.isEmpty() || mobileNumber.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate mobile number format
        if (!mobileNumber.matches("\\d{10}")) {
            Toast.makeText(this, "Enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate password length
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!chkTerms.isChecked()) {
            Toast.makeText(this, "You must accept the terms and privacy policy", Toast.LENGTH_SHORT).show();
            return;
        }

        // Execute registration task
        new RegisterTask().execute(parentName, childName, childAge, childDiagnosis, mobileNumber, password);
    }

    private class RegisterTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Registration.this, "Registering", "Please wait...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            String parentName = params[0];
            String childName = params[1];
            String childAge = params[2];
            String childDiagnosis = params[3];
            String mobileNumber = params[4];
            String password = params[5];

            try {
                URL url = new URL(REGISTER_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Create post data
                HashMap<String, String> postData = new HashMap<>();
                postData.put("parentname", parentName);
                postData.put("childname", childName);
                postData.put("childage", childAge);
                postData.put("childdiagnosis", childDiagnosis);
                postData.put("mobilenumber", mobileNumber);
                postData.put("password", password);

                Writer writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                writer.write(getPostDataString(postData));
                writer.flush();
                writer.close();

                // Read response
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                Log.d("RegistrationResponse", sb.toString()); // Log response for debugging
                return sb.toString();
            } catch (Exception e) {
                Log.e("RegistrationError", "Error: " + e.getMessage(), e);
                return "{\"status\":\"error\",\"message\":\"Connection failed\"}"; // Return JSON error format
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            Log.d("RegistrationResponse", result); // Debugging

            try {
                JSONObject response = new JSONObject(result);
                String status = response.getString("status");
                String message = response.getString("message");

                if (status.equalsIgnoreCase("success")) {
                    Toast.makeText(Registration.this, message, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Registration.this, ParentLogin.class));
                    finish();
                } else {
                    Toast.makeText(Registration.this, "Registration Failed: " + message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(Registration.this, "Unexpected error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("RegistrationError", "Parsing Error: ", e);
            }
        }
    }

    private String getPostDataString(HashMap<String, String> params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) first = false;
            else result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
