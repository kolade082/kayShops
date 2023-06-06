/**

 The LoginActivity class represents the sign in screen of the application.
 It extends the AppCompatActivity class and is responsible for displaying a bar for
 email and password so user can redirected to the Main Activity if a user has an account else to the register screen.
 */
package com.example.kayshops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kayshops.admin.AdminActivity;
import com.example.kayshops.user.UserActivity;
import com.example.kayshops.user.Users;

import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    // Constants for shared preferences keys and error messages
    private static final String SHARED_PREFS_NAME = "MyPrefs";
    private static final String FULL_NAME_KEY = "fullName";
    private static final String IS_LOGGED_IN_KEY = "isLoggedIn";

    private static final String REQUIRED_FIELDS_MESSAGE = "Fields are required";
    private static final String INVALID_EMAIL_MESSAGE = "Please enter a valid email address.";
    private static final String SHORT_PASSWORD_MESSAGE = "Password should be at least 8 characters long.";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid email or password.";

    // Boolean flag to keep track of whether the user is currently logged in
    private boolean isLoggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtPassword = findViewById(R.id.edtPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView signUpTextView = findViewById(R.id.signUpTextView);

        // Set the spannable text for the sign-up text view
        setSpannableText(signUpTextView);

        // Create an instance of the database
        DbConnect dbConnect = new DbConnect(this);

        // Set click listener for the sign-up text view to start the RegisterActivity
        signUpTextView.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Set click listener for the login button to start the UserActivity
        btnLogin.setOnClickListener(view -> {
            // Get the input values from the email and password fields
            String strEmail = edtEmail.getText().toString().trim();
            String strPassword = edtPassword.getText().toString().trim();

            // Check if the email and password fields are empty
            if (strEmail.isEmpty() || strPassword.isEmpty()) {
                showToast(REQUIRED_FIELDS_MESSAGE);
                return;
            }

            // Check if the email is valid
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                showToast(INVALID_EMAIL_MESSAGE);
                return;
            }

            // Check if the password is at least 8 characters long
            if (strPassword.length() < 8) {
                showToast(SHORT_PASSWORD_MESSAGE);
                return;
            }

            Users u1 = new Users(strEmail, strPassword);
            String result;
            try {
                result = dbConnect.checkEmailPassword(u1);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            // If the authentication is successful, save the user's full name in shared preferences and start the UserActivity
            if (result.equals("user") || result.equals("admin")) {
                Users user = dbConnect.getUserByEmail(strEmail);
                // Save the user's full name in shared preferences
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(FULL_NAME_KEY, user.getFullName());
                editor.putString("userType", result);
                editor.putBoolean(IS_LOGGED_IN_KEY, true);
                editor.putInt("userId", user.getUserID());
                editor.putString("email", strEmail);
                editor.apply();
                String fullName = sharedPreferences.getString(FULL_NAME_KEY, "");
                Log.d("LoginACTIVITY", "FULL NAME RETRIEVED: " + fullName);

                showToast("Welcome, " + fullName);

                // Set isLoggedIn flag to true
                isLoggedIn = true;

                // Start the UserActivity or AdminActivity depending on the user type
                if (result.equals("admin")) {
                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                    startActivity(intent);
                }
            } else {
                showToast(INVALID_CREDENTIALS_MESSAGE);
            }

        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Check if the user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN_KEY, false);

        if (isLoggedIn) {
            // Get the user type from shared preferences
            String userType = sharedPreferences.getString("userType", "");

            // Start the appropriate activity based on the user type
            if (userType.equals("admin")) {
                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                startActivity(intent);
            }
        }
    }



//    this method is used to change the colour of the "sign up" to blue and start the register
//    activity when clicked on
    private void setSpannableText(TextView textView) {
        String signUpText = "No account? Sign Up";
        SpannableString spannableString = new SpannableString(signUpText);

        // Set the color of the word "Sign Up" to blue
        int startIndex = signUpText.indexOf("Sign Up");
        int endIndex = startIndex + "Sign Up".length();
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);

        // Set click listener to start RegisterActivity
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

//    This is used to show toast
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // Override the onBackPressed method to clear the isLoggedIn flag and remove the user's full name from shared preferences
    @Override
    public void onBackPressed() {
        // Remove the user's full name from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(FULL_NAME_KEY);
        // Set the isLoggedIn flag to false
        editor.putBoolean(IS_LOGGED_IN_KEY, false);
        editor.apply();

        // Set isLoggedIn flag to false
        isLoggedIn = false;

        super.onBackPressed();
    }
}

