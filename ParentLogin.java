package com.SIMATS.austismapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ParentLogin extends AppCompatActivity {

    private EditText edtMobilenum, edtPassword;
    private Button btnSignIn;
    private TextView tvLoginLink;
    private ImageView btnBack;


    private static final String LOGIN_URL = "http://14.139.187.229:8081/jan2025/autism/login.php";

    private static final String PREF_NAME = "user_prefs"; // SharedPreferences file name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);

        edtMobilenum = findViewById(R.id.edtnumber);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvLoginLink = findViewById(R.id.tv_login_link);
        btnBack = findViewById(R.id.btnBack);

        btnSignIn.setOnClickListener(v -> loginUser());

        tvLoginLink.setOnClickListener(v -> {
            Intent intent = new Intent(ParentLogin.this, Registration.class);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void loginUser() {
        String mobileNumber = edtMobilenum.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (mobileNumber.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new LoginTask().execute(mobileNumber, password);
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ParentLogin.this, "Logging in", "Please wait...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            String mobileNumber = params[0];
            String password = params[1];

            try {
                URL url = new URL(LOGIN_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                HashMap<String, String> postData = new HashMap<>();
                postData.put("mobilenumber", mobileNumber);
                postData.put("password", password);

                Writer writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                writer.write(getPostDataString(postData));
                writer.flush();
                writer.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                return sb.toString();
            } catch (Exception e) {
                Log.e("LoginError", "Error: " + e.getMessage(), e);
                return "{\"status\":\"error\",\"message\":\"Connection failed\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            try {
                JSONObject response = new JSONObject(result);
                String status = response.getString("status");
                String message = response.getString("message");

                if (status.equalsIgnoreCase("success")) {
                    Toast.makeText(ParentLogin.this, message, Toast.LENGTH_SHORT).show();

                    // ✅ Save mobilenumber to SharedPreferences
                    String mobileNumber = edtMobilenum.getText().toString().trim();
                    SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                    prefs.edit().putString("mobilenumber", mobileNumber).apply();

                    // Move to next screen
                    startActivity(new Intent(ParentLogin.this, ChildTrainProgress.class));
                    finish();
                } else {
                    Toast.makeText(ParentLogin.this, "Login Failed: " + message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(ParentLogin.this, "Unexpected error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("LoginError", "Parsing Error: ", e);
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
