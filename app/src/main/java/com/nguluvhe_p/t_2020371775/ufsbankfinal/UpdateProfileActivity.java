package com.nguluvhe_p.t_2020371775.ufsbankfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UpdateProfileActivity extends AppCompatActivity {

   private EditText studentNumberEditText, accountNumberEditText, emailEditText, balanceEditText,
           nameEditText, addressEditText, cityEditText, postCodeEditText, passwordEditText;
   private DatabaseHelper myDb;
   int customerId; // To store the customer ID
   Button updateButton;

   @SuppressLint("Range")
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_update_profile);

      // Initialize the database helper
      myDb = new DatabaseHelper(this);

      // Retrieve the customer ID passed from the previous activity
      if (getIntent() != null) {
         customerId = getIntent().getIntExtra("customer_id", -1);
      }

      // Initialize the views
      studentNumberEditText = findViewById(R.id.studentNumberEditText);
      accountNumberEditText = findViewById(R.id.accountNumberEditText);
      emailEditText = findViewById(R.id.emailEditText);
      balanceEditText = findViewById(R.id.balanceEditText);
      nameEditText = findViewById(R.id.nameEditText);
      addressEditText = findViewById(R.id.addressEditText);
      cityEditText = findViewById(R.id.cityEditText);
      postCodeEditText = findViewById(R.id.postCodeEditText);
      passwordEditText = findViewById(R.id.passwordEditText);
      updateButton = findViewById(R.id.updateButton);

      // Populate the fields with existing customer data
      populateCustomerData(customerId);

      // Set the click listener for the update button
      updateButton.setOnClickListener(v -> updateCustomerDetails());

      int currentUserId = getIntent().getIntExtra("customer_id", -1);

      BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_cus);

      bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
         int itemId = item.getItemId();
         if (itemId == R.id.nav_transfer) {// Navigate to Transfer screen
            Intent transferIntent = new Intent(UpdateProfileActivity.this, TransferActivity.class);
            transferIntent.putExtra("customer_id", currentUserId);
            startActivity(transferIntent);
            return true;
         } else if (itemId == R.id.nav_update_profile) {// Navigate to Update Profile screen
            Intent updateProfileIntent = new Intent(UpdateProfileActivity.this, UpdateProfileActivity.class);

            // Pass the logged-in user's details (if needed)
            String studentNumber = getIntent().getStringExtra("student_number");
            updateProfileIntent.putExtra("student_number", studentNumber);

            startActivity(updateProfileIntent);
            return true;
         }
         return false;
      });
   }

   // Method to populate customer data into form fields
   @SuppressLint("Range")
   private void populateCustomerData(int customerId) {
      Cursor cursor = myDb.getCustomerById(customerId);
      if (cursor != null && cursor.moveToFirst()) {
         studentNumberEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_2)));
         accountNumberEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_3)));
         emailEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_4)));
         balanceEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_5)));
         nameEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_6)));
         addressEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_7)));
         cityEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_8)));
         postCodeEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_9)));
         passwordEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_10)));
      }
      if (cursor != null) {
         cursor.close();
      }
   }

   // Method to update customer details
   private void updateCustomerDetails() {
      boolean isUpdated = myDb.updateCustomer(
              customerId,
              studentNumberEditText.getText().toString(),
              accountNumberEditText.getText().toString(),
              emailEditText.getText().toString(),
              balanceEditText.getText().toString(),
              nameEditText.getText().toString(),
              addressEditText.getText().toString(),
              cityEditText.getText().toString(),
              postCodeEditText.getText().toString(),
              passwordEditText.getText().toString());

      if (isUpdated) {
         Toast.makeText(UpdateProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
         finish(); // Close the activity and return to the previous one
      } else {
         Toast.makeText(UpdateProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
      }
   }
}
