package com.nguluvhe_p.t_2020371775.ufsbankfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TransferActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText recipientAccountNumber, transferAmount;
    Button confirmTransferButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        myDb = new DatabaseHelper(this);
        int currentUserId = getIntent().getIntExtra("customer_id", -1);

        recipientAccountNumber = findViewById(R.id.recipientAccountNumber);
        transferAmount = findViewById(R.id.transferAmount);
        confirmTransferButton = findViewById(R.id.confirmTransferButton);

        confirmTransferButton.setOnClickListener(v -> {
            String recipientAccount = recipientAccountNumber.getText().toString();
            String amount = transferAmount.getText().toString();

            // Validate inputs and initiate transfer
            if (validateInputs(recipientAccountNumber, transferAmount)) {
                boolean transferSuccess = myDb.transferMoney(currentUserId, recipientAccount, amount); // Pass the values as strings

                if (transferSuccess) {
                    Toast.makeText(TransferActivity.this, "Transfer successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TransferActivity.this, CustomerActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(TransferActivity.this, "Transfer failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(TransferActivity.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_cus);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_transfer) {// Navigate to Transfer screen
                Intent transferIntent = new Intent(TransferActivity.this, TransferActivity.class);
                transferIntent.putExtra("customer_id", currentUserId);
                startActivity(transferIntent);
                return true;
            } else if (itemId == R.id.nav_update_profile) {// Navigate to Update Profile screen
                Intent updateProfileIntent = new Intent(TransferActivity.this, UpdateProfileActivity.class);

                // Pass the logged-in user's details (if needed)
                String studentNumber = getIntent().getStringExtra("student_number");
                updateProfileIntent.putExtra("student_number", studentNumber);

                startActivity(updateProfileIntent);
                return true;
            }
            return false;
        });

    }
    private boolean validateInputs(EditText recipientAccount, EditText amount) {
        String accountNumber = recipientAccount.getText().toString();
        String transferAmount = amount.getText().toString();

        // Check if the fields are empty
        if (accountNumber.isEmpty() || transferAmount.isEmpty()) {
            return false; // Invalid inputs
        }

        // Check if the transfer amount is a valid number
        try {
            double amountValue = Double.parseDouble(transferAmount);
            return amountValue > 0; // Ensure the amount is positive
        } catch (NumberFormatException e) {
            return false; // Not a valid number
        }
    }

}
