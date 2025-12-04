package com.SIMATS.austismapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MenuSettings extends AppCompatActivity {

    private ImageView backButton;
    private LinearLayout homeButton, childDetailsButton, progressButton, signOutButton;
    private TextView childName;

    // Use your PC IP for real Android device OR
    // Use 10.0.2.2 for emulator
    private final String DATA_URL = "http://14.139.187.229:8081/jan2025/autism/child_details.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_settings);

        // Initialize Views
        backButton = findViewById(R.id.backButton);
        homeButton = findViewById(R.id.home_button);
        childDetailsButton = findViewById(R.id.child_details_button);
        progressButton = findViewById(R.id.progress_button);
        signOutButton = findViewById(R.id.sign_out_button);
        childName = findViewById(R.id.child_name);

        // Fetch child name
        fetchChildName();

        // Back Button
        backButton.setOnClickListener(v -> finish());

        homeButton.setOnClickListener(v -> {
            startActivity(new Intent(MenuSettings.this, ChildTrainProgress.class));
        });

        childDetailsButton.setOnClickListener(v -> {
            startActivity(new Intent(MenuSettings.this, ChildInformation.class));
        });

        progressButton.setOnClickListener(v -> {
            startActivity(new Intent(MenuSettings.this, ChildProgress.class));
        });

        signOutButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            prefs.edit().clear().apply();

            startActivity(new Intent(MenuSettings.this, ParentDoctorActivity.class));
            finish();
        });
    }

    private void fetchChildName() {
        new Thread(() -> {
            try {
                SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                String mobilenumber = prefs.getString("mobilenumber", "");

                if (mobilenumber.isEmpty()) {
                    runOnUiThread(() ->
                            Toast.makeText(MenuSettings.this, "Mobile number not found", Toast.LENGTH_SHORT).show());
                    return;
                }

                URL url = new URL(DATA_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String postData = "mobilenumber=" + URLEncoder.encode(mobilenumber, "UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);

                StringBuilder result = new StringBuilder();
                int data = reader.read();

                while (data != -1) {
                    result.append((char) data);
                    data = reader.read();
                }

                Log.d("API Response", result.toString());

                JSONObject jsonObject = new JSONObject(result.toString());
                String name = jsonObject.getString("childname");

                new Handler(Looper.getMainLooper()).post(() ->
                        childName.setText(name));

            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(MenuSettings.this, "Failed to load name", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
