/**

 The IntroActivity class represents the introductory screen of the application.
 It extends the AppCompatActivity class and is responsible for displaying the
 introductory screen for a short duration before redirecting the user to the
 LoginActivity screen.
 */
package com.example.kayshops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Show the progress bar
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Wait for 2 seconds before redirecting to the LoginActivity
        new Handler().postDelayed(() -> {
            Intent loginIntent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }, 2000);

    }
}