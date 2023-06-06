/**

 This activity is responsible for handling the user registration process.
 It includes user input fields for full name, password, email, date of birth, phone number, and address.
 It uses a DbConnect object to add a new user to the database upon successful registration.
 It also includes validation for user inputs, including checking for required fields, valid phone numbers, valid email addresses, and strong passwords.
 */
package com.example.kayshops;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kayshops.user.Users;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    EditText edtDateOfBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText edtFullName = findViewById(R.id.edtFullName);
        EditText edtPassword = findViewById(R.id.edtPassword);
        edtDateOfBirth = findViewById(R.id.edtDateOfBirth);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtPhoneNo = findViewById(R.id.edtPhoneNo);
        EditText edtAddressStreet = findViewById(R.id.edtAddressStreet);
        EditText edtAddressCity = findViewById(R.id.edtAddressCity);
        EditText edtAddressCounty = findViewById(R.id.edtAddressCounty);
        EditText edtpostcode = findViewById(R.id.edtAddressPostCode);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignUp = findViewById(R.id.btnSignUp);

        // Create an instance of the database
        DbConnect dbConnect = new DbConnect(this);

        edtDateOfBirth.setOnClickListener(view -> showDatePickerDialog());

        btnLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnSignUp.setOnClickListener(view -> {
            String strFullName = edtFullName.getText().toString();
            String strPassword = edtPassword.getText().toString();
            String strEmail = edtEmail.getText().toString();
            String strDateOfBirth = edtDateOfBirth.getText().toString();
            String strPhoneNumber = edtPhoneNo.getText().toString();
            String strStreetAddress = edtAddressStreet.getText().toString();
            String strCityAddress = edtAddressCity.getText().toString();
            String strCountyAddress = edtAddressCounty.getText().toString();
            String strPostcode = edtpostcode.getText().toString();

            if (strFullName.isEmpty() && strPassword.isEmpty() && strEmail.isEmpty() && strDateOfBirth.isEmpty() &&
                    strPhoneNumber.isEmpty() && strStreetAddress.isEmpty() && strCityAddress.isEmpty() &&
                    strCountyAddress.isEmpty() && strPostcode.isEmpty()){
                // If any required fields are empty, show a toast indicating that all fields are required
                Toast.makeText(RegisterActivity.this,
                        " Fields are required ",
                        Toast.LENGTH_LONG).show();
            }else if (!strPhoneNumber.matches("\\d{11}")) {
                // If the phone number is not valid, show a toast indicating that a valid phone number is required
                Toast.makeText(RegisterActivity.this,
                        "Please enter a valid 10-digit phone number.",
                        Toast.LENGTH_LONG).show();
            }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                // If the email address is not valid, show a toast indicating that a valid email address is required
                Toast.makeText(RegisterActivity.this, "Please enter a valid email address.", Toast.LENGTH_LONG).show();
                return;
            }else if (!strPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
                // If the email address is not valid, show a toast indicating that a password is required
                Toast.makeText(RegisterActivity.this, "Password must contain at least 8 characters including at least one uppercase letter, one lowercase letter, one number, and one special character.", Toast.LENGTH_LONG).show();
                return;
            } else{
                Users newUser = new Users(strFullName, strPassword, strDateOfBirth, strEmail,
                        strPhoneNumber, strStreetAddress, strCityAddress, strCountyAddress, strPostcode);
                try {
                    dbConnect.addUser(newUser);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
                // If all details are valid, it shows a toast indicating user has been successfully registered
                Toast.makeText(RegisterActivity.this,
                        " Successfully Registered ",
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                dbConnect.close();
            }
        });

    }

// this method is used to display a calender making it easier for user to select their date of birth
    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, (datePicker, year1, month1, day1) -> {
            // Update the "Date of Birth" EditText field with the selected date
            edtDateOfBirth.setText(day1 + "/" + (month1 + 1) + "/" + year1);
        }, year, month, day);

        datePickerDialog.show();
    }

}