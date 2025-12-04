package com.SIMATS.austismapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SocialskillEyecontact extends AppCompatActivity {

    private SeekBar seekBar1, seekBar2, seekBar3, seekBar4;
    private TextView progressText1, progressText2, progressText3, progressText4;
    private Button nextButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socialskill_eyecontact);

        // Initialize UI elements
        seekBar1 = findViewById(R.id.seekBar);
        seekBar2 = findViewById(R.id.seekBar1);
        seekBar3 = findViewById(R.id.seekBar2);
        seekBar4 = findViewById(R.id.seekBar3);

        progressText1 = findViewById(R.id.progressText);
        progressText2 = findViewById(R.id.progress1Text);
        progressText3 = findViewById(R.id.progress2Text);
        progressText4 = findViewById(R.id.progress3Text);

        nextButton = findViewById(R.id.btn_next);
        backButton = findViewById(R.id.backButton);

        // Handle Back Button Click
        backButton.setOnClickListener(v -> finish());

        // Setup SeekBar listeners
        setupSeekBar(seekBar1, progressText1);
        setupSeekBar(seekBar2, progressText2);
        setupSeekBar(seekBar3, progressText3);
        setupSeekBar(seekBar4, progressText4);

        // Handle Next Button Click
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(SocialskillEyecontact.this, SocialSkillPointing.class);
            startActivity(intent);
        });
    }

    private void setupSeekBar(SeekBar seekBar, TextView progressText) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}
