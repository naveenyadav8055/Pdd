package com.SIMATS.austismapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ParentInformation extends AppCompatActivity {

    private TableLayout parentTable;
    private ImageButton backButton, menuButton;
    private static final String FETCH_URL = "http://14.139.187.229:8081/jan2025/autism/parentinformation.php";
    //private static final String FETCH_URL = "http://192.168.1.4/API/parentinformation.php"; //for wifi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_information);

        // Initialize UI components
        parentTable = findViewById(R.id.parentTable);
        backButton  = findViewById(R.id.back_button);
        menuButton  = findViewById(R.id.menu_button);

        // Set OnClickListener for the back button
        backButton.setOnClickListener(v -> finish());

        // Set OnClickListener for the menu button
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(ParentInformation.this, MenuSettings.class);
            startActivity(intent);
        });

        // Fetch data
        new FetchParentTask().execute();
    }

    private class FetchParentTask extends AsyncTask<Void, Void, JSONArray> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(ParentInformation.this, "Fetching Data", "Please wait...", true);
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            try {
                URL url = new URL(FETCH_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream in = conn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);

                return new JSONArray(sb.toString());
            } catch (Exception e) {
                Log.e("FetchError", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray arr) {
            pd.dismiss();
            if (arr == null) {
                Toast.makeText(ParentInformation.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                return;
            }

            // Remove old data rows (keep header at position 0)
            int headerCount = 1;
            while (parentTable.getChildCount() > headerCount) {
                parentTable.removeViewAt(headerCount);
            }

            // Add one TableRow per JSON record
            for (int i = 0; i < arr.length(); i++) {
                try {
                    JSONObject obj = arr.getJSONObject(i);
                    String name  = obj.getString("childname");
                    String age   = obj.getString("childage");
                    String diag  = obj.getString("childdiagnosis");
                    String mob   = obj.getString("mobilenumber");
                    addDataRow(name, age, diag, mob);
                } catch (Exception e) {
                    Log.e("ParseError", e.getMessage(), e);
                }
            }
        }
    }

    private void addDataRow(String name, String age, String diag, String mob) {
        TableRow row = new TableRow(this);

        TextView cName = new TextView(this);
        cName.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        cName.setPadding(4,4,4,4);
        cName.setText(name);
        cName.setTextColor(getResources().getColor(android.R.color.black));

        TextView cAge = new TextView(this);
        cAge.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        cAge.setPadding(4,4,4,4);
        cAge.setText(age);
        cAge.setTextColor(getResources().getColor(android.R.color.black));

        TextView cDiag = new TextView(this);
        cDiag.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        cDiag.setPadding(4,4,4,4);
        cDiag.setText(diag);
        cDiag.setTextColor(getResources().getColor(android.R.color.black));

        TextView cMob = new TextView(this);
        cMob.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        cMob.setPadding(4,4,4,4);
        cMob.setText(mob);
        cMob.setTextColor(getResources().getColor(android.R.color.black));

        row.addView(cName);
        row.addView(cAge);
        row.addView(cDiag);
        row.addView(cMob);

        parentTable.addView(row);
    }
}
