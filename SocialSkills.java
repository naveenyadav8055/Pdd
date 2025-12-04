package com.SIMATS.austismapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SocialSkills extends AppCompatActivity {

    private ImageView backIcon, notificationIcon, imageIllustration;
    private Button btnIntroduction, btnEyeContactSkills, btnPointingSkills, btnImitationSkill, btnJointAttentionSkills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_skills); // Make sure this matches your XML file name

        // Initialize UI components
        backIcon = findViewById(R.id.back_icon);
        notificationIcon = findViewById(R.id.notification_icon);
        imageIllustration = findViewById(R.id.image_illustration);
        btnIntroduction = findViewById(R.id.btn_introduction);
        btnEyeContactSkills = findViewById(R.id.btn_eye_contanct_skills);
        btnPointingSkills = findViewById(R.id.btn_pointing_skills);
        btnImitationSkill = findViewById(R.id.btn_imitation_skill);
        btnJointAttentionSkills = findViewById(R.id.btn_joint_attention_skills);

        // Back button - navigate back
        backIcon.setOnClickListener(v -> finish());

        // Notification icon - placeholder action
        notificationIcon.setOnClickListener(v -> {
            // Add notification logic if needed
        });

        // Navigate to IntroductionActivity
        btnIntroduction.setOnClickListener(v -> {
            Intent intent = new Intent(SocialSkills.this, SocialskillIntro.class);
            startActivity(intent);
        });

        // Navigate to EyeContactSkillsActivity
        btnEyeContactSkills.setOnClickListener(v -> {
            Intent intent = new Intent(SocialSkills.this, SocialskillEyecontact.class);
            startActivity(intent);
        });

        // Navigate to PointingSkillsActivity
        btnPointingSkills.setOnClickListener(v -> {
            Intent intent = new Intent(SocialSkills.this, SocialSkillPointing.class);
            startActivity(intent);
        });

        // Navigate to ImitationSkillActivity
        btnImitationSkill.setOnClickListener(v -> {
            Intent intent = new Intent(SocialSkills.this, SocialSkillImitation.class);
            startActivity(intent);
        });

        // Navigate to JointAttentionSkillsActivity
        btnJointAttentionSkills.setOnClickListener(v -> {
            Intent intent = new Intent(SocialSkills.this, SocialSkillAttention.class);
            startActivity(intent);
        });
    }
}
