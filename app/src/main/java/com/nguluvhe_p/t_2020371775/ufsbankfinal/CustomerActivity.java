package com.nguluvhe_p.t_2020371775.ufsbankfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerActivity extends AppCompatActivity {
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer); // Set your layout

        // Initialize your DatabaseHelper
        myDb = new DatabaseHelper(this);

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logout());

        int currentUserId = getIntent().getIntExtra("customer_id", -1);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_cus);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_transfer) {// Navigate to Transfer screen
                Intent transferIntent = new Intent(CustomerActivity.this, TransferActivity.class);
                transferIntent.putExtra("customer_id", currentUserId);
                startActivity(transferIntent);
                return true;
            } else if (itemId == R.id.nav_update_profile) {// Navigate to Update Profile screen
                Intent updateProfileIntent = new Intent(CustomerActivity.this, UpdateProfileActivity.class);

                // Pass the logged-in user's details (if needed)
                String studentNumber = getIntent().getStringExtra("student_number");
                updateProfileIntent.putExtra("student_number", studentNumber);

                startActivity(updateProfileIntent);
                return true;
            }
            return false;
        }); // Ensure this ID matches your XML layout

        // Assume you pass the currentUserId from the login process using Intent
        // Use -1 as default

        // Check if currentUserId is valid
        if (currentUserId != -1) {
            // Fetch customer details
            Cursor cursor = myDb.getCustomerById(currentUserId); // Make sure this method is correct
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String accountNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_3)); // Account number
                @SuppressLint("Range") String balance = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_5)); // Balance

                // Inflate the banner layout
                LayoutInflater inflater = getLayoutInflater();
                View banner = inflater.inflate(R.layout.layout_banner, null);

                // Set account number and balance
                TextView accountNumberTextView = banner.findViewById(R.id.accountNumberTextView);
                TextView balanceTextView = banner.findViewById(R.id.balanceTextView);
                accountNumberTextView.setText("Account Number: " + accountNumber);
                balanceTextView.setText("Balance: R" + balance);

                // Add the banner to your main layout
                LinearLayout mainLayout = findViewById(R.id.mainLayout); // Main layout container
                mainLayout.addView(banner);
            }

            // Close the cursor when done
            if (cursor != null) {
                cursor.close();
            }
        } else {
            // Handle the error case when currentUserId is invalid
            // For example, you can show a Toast message or redirect to the login screen
            finish(); // Close the activity
        }
    }
    private void logout() {
        // Navigate back to LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear activity stack
        startActivity(intent);
        finish(); // Close the current activity
    }
}
