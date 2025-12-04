package com.SIMATS.austismapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class ChildTrainProgress extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_train_progress);

        // Back button - Navigate back
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // Menu button - Placeholder action (you can add a menu feature later)
        ImageButton menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChildTrainProgress.this, MenuSettings.class);
            startActivity(intent);
        });


        // Training Button - Navigate to Training Screen
        Button trainingButton = findViewById(R.id.btn_training);
        trainingButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChildTrainProgress.this, ChildTraining.class);
            startActivity(intent);
        });

        // Progress Button - Navigate to Progress Screen
        Button progressButton = findViewById(R.id.btn_progress);
        progressButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChildTrainProgress.this, ChildProgress.class);
            startActivity(intent);
        });
    }
}
