package com.SIMATS.austismapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class CompletedPlayskills extends AppCompatActivity {

    private ImageView backIcon, notificationIcon, imageIllustration;
    private Button btnSocialSkills, btnPlaySkills, btnSelfHelpSkills, btnBehavioralIssues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_playskills);  // Ensure this matches your XML layout file

        // Initialize UI components
        backIcon = findViewById(R.id.back_icon);
        notificationIcon = findViewById(R.id.notification_icon);
        imageIllustration = findViewById(R.id.image_illustration);
        btnSocialSkills = findViewById(R.id.btn_social_skills);
        btnPlaySkills = findViewById(R.id.btn_play_skills);
        btnSelfHelpSkills = findViewById(R.id.btn_self_help_skills);
        btnBehavioralIssues = findViewById(R.id.btn_behavioral_issues);

        // Back button - navigate back
        backIcon.setOnClickListener(v -> finish());

        // Notification icon - placeholder action
        notificationIcon.setOnClickListener(v -> {
            // Add notification logic here if needed
        });

        // Navigate to SocialSkillsActivity
        btnSocialSkills.setOnClickListener(v -> {
            Intent intent = new Intent(CompletedPlayskills.this, SocialSkills.class);
            startActivity(intent);
        });

        // Navigate to PlaySkillsActivity
        btnPlaySkills.setOnClickListener(v -> {
            Intent intent = new Intent(CompletedPlayskills.this, PlaySkills.class);
            startActivity(intent);
        });

        // Navigate to SelfHelpSkillsActivity
        btnSelfHelpSkills.setOnClickListener(v -> {
            Intent intent = new Intent(CompletedPlayskills.this, SelfHelpSkills.class);
            startActivity(intent);
        });

        // Navigate to BehavioralIssuesActivity
        btnBehavioralIssues.setOnClickListener(v -> {
            Intent intent = new Intent(CompletedPlayskills.this, BehavioralIssues.class);
            startActivity(intent);
        });
    }
}
