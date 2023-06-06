package com.example.kayshops.admin;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kayshops.DbConnect;
import com.example.kayshops.R;
import com.example.kayshops.user.Users;

public class UpdateUserActivity extends AppCompatActivity {
    private Users users;
    Button updateUserButton;
    EditText fullNameEditText, dateOfBirthEditText, emailEditText, phoneNumberEditText, userTypeEditText;

    // Create an instance of the database
    DbConnect dbConnect = new DbConnect(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        users = (Users) getIntent().getSerializableExtra("user");

        fullNameEditText = findViewById(R.id.fullNameEditText);
        dateOfBirthEditText = findViewById(R.id.dateOfBirthEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        userTypeEditText = findViewById(R.id.userTypeEditText);

        updateUserButton = findViewById(R.id.update_user_btn);

        if (users != null) {
            fullNameEditText.setText(users.getFullName());
            dateOfBirthEditText.setText(users.getDateOfBirth());
            emailEditText.setText(users.getEmail());
            phoneNumberEditText.setText(users.getPhoneNumber());
            userTypeEditText.setText(users.getUserType());
        }

        updateUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    Users updatedUser = getUpdatedUser();
                    updateUserInDatabase(updatedUser);
                } else {
                    Toast.makeText(UpdateUserActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateFields() {
        String fullName = fullNameEditText.getText().toString();
        String dateOfBirth = dateOfBirthEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String userType = userTypeEditText.getText().toString();

        return !fullName.isEmpty() && !dateOfBirth.isEmpty() && !email.isEmpty() && !phoneNumber.isEmpty() && !userType.isEmpty();
    }

    private Users getUpdatedUser() {
        String updatedFullName = fullNameEditText.getText().toString();
        String updatedDateOfBirth = dateOfBirthEditText.getText().toString();
        String updatedEmail = emailEditText.getText().toString();
        String updatedPhoneNumber = phoneNumberEditText.getText().toString();
        String updatedUserType = userTypeEditText.getText().toString();

        int userId = users.getUserID();
        Users updatedUser = new Users(userId, updatedFullName, updatedDateOfBirth, updatedEmail, updatedPhoneNumber, updatedUserType);
        return updatedUser;
    }

    private void updateUserInDatabase(Users updatedUser) {
        boolean isUpdated = dbConnect.updateUser(updatedUser);
        if (isUpdated) {
            Toast.makeText(UpdateUserActivity.this, "User Updated Successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(UpdateUserActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
