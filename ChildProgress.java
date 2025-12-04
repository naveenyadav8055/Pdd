package com.SIMATS.austismapp;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ChildProgress extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_progress);

        // Back button - Navigate back
        ImageView backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(v -> finish());
    }
}
