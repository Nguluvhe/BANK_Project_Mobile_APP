package com.nguluvhe_p.t_2020371775.ufsbankfinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText studentNumber, password;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myDb = new DatabaseHelper(this);
        studentNumber = findViewById(R.id.studentNumber);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        ImageView logoImageView = findViewById(R.id.logoImageView);

        // Load the bounce animation
        Animation bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);

        logoImageView.startAnimation(bounceAnimation);

        new Handler().postDelayed(logoImageView::clearAnimation, 4000);

        // Insert test data for initial testing
        myDb.createTestData();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userStudentNumber = studentNumber.getText().toString();
                String userPassword = password.getText().toString();

                // Check login and get role
                String role = myDb.checkLogin(userStudentNumber, userPassword);

                if (role != null) {
                    // Fetch customer ID if the role is "Customer"
                    switch (role) {
                        case "Admin": {
                            Toast.makeText(LoginActivity.this, "Login as Admin", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case "Consultant": {
                            Toast.makeText(LoginActivity.this, "Login as Consultant", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case "FinancialAdvisor": {
                            Toast.makeText(LoginActivity.this, "Login as FinancialAdvisor", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, FinancialAdvisorActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case "Customer":
                            // Fetch the customer ID based on student number
                            int customerId = myDb.getCustomerIdByStudentNumber(userStudentNumber); // Make sure this method exists

                            if (customerId != -1) { // Assuming -1 indicates an invalid customer ID
                                Toast.makeText(LoginActivity.this, "Login as Customer", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, CustomerActivity.class);
                                intent.putExtra("customer_id", customerId); // Pass the customer ID
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Customer not found", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
