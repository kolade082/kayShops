package com.example.kayshops.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kayshops.DbConnect;
import com.example.kayshops.R;

import java.security.NoSuchAlgorithmException;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText oldPasswordEditText, newPasswordEditText, confirmNewPasswordEditText;
    Button submitButton;
    private static final String SHARED_PREFS_NAME = "MyPrefs";
    private static final String PASSWORD_KEY = "password";
    private static final String USER_ID_KEY = "userId";  // assuming you save user's id in shared prefs with this key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmNewPasswordEditText = findViewById(R.id.confirmNewPasswordEditText);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(view -> {
            String oldPassword = oldPasswordEditText.getText().toString();
            String newPassword = newPasswordEditText.getText().toString();
            String confirmNewPassword = confirmNewPasswordEditText.getText().toString();

            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
            int userId = sharedPreferences.getInt(USER_ID_KEY, -1);

            if (userId == -1) {
                Toast.makeText(this, "User id not found!", Toast.LENGTH_SHORT).show();
                return;
            }

            DbConnect dbConnect = new DbConnect(this);
            try {
                boolean isPasswordCorrect = dbConnect.isPasswordCorrect(userId, oldPassword);
                if (!isPasswordCorrect) {
                    oldPasswordEditText.setError("Incorrect old password!");
                    return;
                }

                if (!newPassword.equals(confirmNewPassword)) {
                    confirmNewPasswordEditText.setError("Passwords do not match!");
                    return;
                }

                boolean isPasswordUpdated = dbConnect.updatePassword(userId, newPassword);
                if (isPasswordUpdated) {
                    Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });

    }
}


