package com.example.kayshops.user;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kayshops.LoginActivity;
import com.example.kayshops.R;

public class ProfileFragment extends Fragment {
    TextView ordersTextView, profileTextView, changePasswordTextView, addressTextView;
    ImageView profilePictureImageView;
    private static final String SHARED_PREFS_NAME = "MyPrefs";
    private static final String FULL_NAME_KEY = "fullName";
    private static final String USER_ID_KEY = "userId";
    private static final String IS_LOGGED_IN_KEY = "isLoggedIn";
    private static final String PROFILE_PIC_KEY = "profilePic";
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final int PICK_IMAGE = 100;


    // Boolean flag to keep track of whether the user is currently logged in
    private boolean isLoggedIn = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Retrieve the user's full name from shared preferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String fullName = sharedPreferences.getString(FULL_NAME_KEY, "");
        String userId = String.valueOf(sharedPreferences.getInt(USER_ID_KEY, 0));

        String profilePicPath = sharedPreferences.getString(PROFILE_PIC_KEY + userId, "");


        // Setting the text of the TextView with the retrieved user's full name
        TextView fullNameTextView = view.findViewById(R.id.fullNameTextView);
        fullNameTextView.setText(fullName);

        profilePictureImageView = view.findViewById(R.id.profilePictureImageView);

        ordersTextView = view.findViewById(R.id.ordersTextView);
        profileTextView = view.findViewById(R.id.profileTextView);
        changePasswordTextView = view.findViewById(R.id.changePasswordTextView);
        addressTextView = view.findViewById(R.id.addressTextView);
        Button logoutButton = view.findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(view1 -> {
            // Remove the user's full name from shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(FULL_NAME_KEY);
            // Set the isLoggedIn flag to false
            editor.remove(IS_LOGGED_IN_KEY);
            editor.apply();

            // Set isLoggedIn flag to false
            isLoggedIn = false;

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        profilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        if (!profilePicPath.equals("")) {
            Bitmap bitmap = BitmapFactory.decodeFile(profilePicPath);
            profilePictureImageView.setImageBitmap(bitmap);
        }

        ordersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewUserOrdersActivity.class);
                startActivity(intent);
            }
        });
        changePasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        addressTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getIntent();
                Users user = (Users) intent.getSerializableExtra("user");

                if (user != null) {
                    Intent updateAddressIntent = new Intent(getActivity(), UpdateAddressActivity.class);
                    updateAddressIntent.putExtra("user", user);
                    startActivity(updateAddressIntent);
                } else {
                    // Handle the case when the user object is null
                    Toast.makeText(getActivity(), "User information not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getActivity(), "Permission denied to read External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String userId = String.valueOf(sharedPreferences.getInt(USER_ID_KEY, 0));
            editor.putString(PROFILE_PIC_KEY + userId, filePath);
            editor.apply();

            // Setting the profile picture
            profilePictureImageView.setImageBitmap(bitmap);
        }
    }
}