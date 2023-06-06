package com.example.kayshops.user;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kayshops.DbConnect;
import com.example.kayshops.R;
import com.example.kayshops.user.Users;

public class UpdateAddressActivity extends AppCompatActivity {
    private Users user;
    Button updateAddressButton;
    EditText streetEditText, cityEditText, countyEditText, postcodeEditText;

    // Create an instance of the database
    DbConnect dbConnect = new DbConnect(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);

        user = (Users) getIntent().getSerializableExtra("user");

        streetEditText = findViewById(R.id.edtAddressStreet);
        cityEditText = findViewById(R.id.edtAddressCity);
        countyEditText = findViewById(R.id.edtAddressCounty);
        postcodeEditText = findViewById(R.id.edtAddressPostCode);

        updateAddressButton = findViewById(R.id.saveButton);


        if (user != null) {
            streetEditText.setText(user.getStreetAddress());
            cityEditText.setText(user.getCityAddress());
            countyEditText.setText(user.getCountyAddress());
            postcodeEditText.setText(user.getPostcode());
        }

        updateAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    Users updatedUser = getUpdatedUser();
                    updateUserInDatabase(updatedUser);
                } else {
                    Toast.makeText(UpdateAddressActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateFields() {
        String street = streetEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String county = countyEditText.getText().toString();
        String postcode = postcodeEditText.getText().toString();

        return !street.isEmpty() && !city.isEmpty() && !county.isEmpty() && !postcode.isEmpty();
    }

    private Users getUpdatedUser() {
        String updatedStreet = streetEditText.getText().toString();
        String updatedCity = cityEditText.getText().toString();
        String updatedCounty = countyEditText.getText().toString();
        String updatedPostcode = postcodeEditText.getText().toString();

        int userId = user.getUserID();
        Users updatedUser = new Users(userId, user.getStreetAddress(), user.getCityAddress(), user.getCountyAddress(), user.getPostcode());
        updatedUser.setStreetAddress(updatedStreet);
        updatedUser.setCityAddress(updatedCity);
        updatedUser.setCountyAddress(updatedCounty);
        updatedUser.setPostcode(updatedPostcode);
        return updatedUser;
    }

    private void updateUserInDatabase(Users updatedUser) {
        boolean isUpdated = dbConnect.updateUserAddress(updatedUser); // change here
        if (isUpdated) {
            Toast.makeText(UpdateAddressActivity.this, "Address Updated Successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(UpdateAddressActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
