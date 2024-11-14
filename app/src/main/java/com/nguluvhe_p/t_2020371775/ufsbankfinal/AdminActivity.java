package com.nguluvhe_p.t_2020371775.ufsbankfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logout());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            if (item.getItemId() == R.id.nav_view_customers) {
                selectedFragment = new ViewCustomersFragment();
            } else if (item.getItemId() == R.id.nav_add_customer) {
                selectedFragment = new AddCustomerFragment();
            }  else {
                return false; // Handle default case
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
            return true;
        });

        // Set default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ViewCustomersFragment()).commit();
    }
    private void logout() {
        // Navigate back to LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear activity stack
        startActivity(intent);
        finish(); // Close the current activity
    }
}
