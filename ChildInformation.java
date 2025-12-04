package com.SIMATS.austismapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChildInformation extends AppCompatActivity {

    private TextView userName, userDetails;
    private ImageView profileImage;
    private ImageButton backButton; // Added back button

    private static final String FETCH_URL = "http://10.228.151.54/details.php"; // Update with your API URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_information);

        // Initialize UI Components
        userName = findViewById(R.id.userName);
        userDetails = findViewById(R.id.userDetails);
        profileImage = findViewById(R.id.profileImage);
        backButton = findViewById(R.id.back_button); // Initialize back button

        // Set back button click listener
        backButton.setOnClickListener(v -> finish()); // Closes the activity and returns to the previous screen

        ImageButton menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChildInformation.this, MenuSettings.class);
            startActivity(intent);
        });

        // Fetch Data
        new FetchParentTask().execute();
    }

    private class FetchParentTask extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ChildInformation.this, "Fetching Data", "Please wait...", true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(FETCH_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                return sb.toString();
            } catch (Exception e) {
                Log.e("FetchError", "Error: " + e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    if (jsonArray.length() > 0) {
                        JSONObject obj = jsonArray.getJSONObject(0); // Assuming the first entry is the parent

                        String parentName = obj.getString("parentname");
                        String childName = obj.getString("childname");
                        String childAge = obj.getString("childage");
                        String childDiagnosis = obj.getString("childdiagnosis");

                        // Update UI Components with Parent Details
                        userName.setText(parentName);
                        userDetails.setText("Child: " + childName + ", Age: " + childAge + ", Diagnosis: " + childDiagnosis);
                    } else {
                        Toast.makeText(ChildInformation.this, "No parent records found", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(ChildInformation.this, "Parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("ParsingError", "Error: ", e);
                }
            } else {
                Toast.makeText(ChildInformation.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
