package com.SIMATS.austismapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PlaySkillIntroduction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socialskill_intro);

        // Back Button
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish()); // Closes the activity

        // Notification Button
        ImageButton notificationButton = findViewById(R.id.notification_button);
        notificationButton.setOnClickListener(v ->
                Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show()
        );

        // Play Button (To Play Video)
        ImageButton playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(v -> openVideo());

        // Next Button (Move to Next Screen)
        Button nextButton = findViewById(R.id.btn_next);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlaySkillIntroduction.this, PlaySkillPlayingTogether.class);
            startActivity(intent);
        });
    }

    private void openVideo() {
        // URL of the video (You can replace it with your local or online video link)
        String videoUrl = "https://www.example.com/sample_video.mp4";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(videoUrl), "video/*");
        startActivity(intent);
    }
}
