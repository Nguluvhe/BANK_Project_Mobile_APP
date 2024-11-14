package com.nguluvhe_p.t_2020371775.ufsbankfinal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CustomerCursorAdapter extends CursorAdapter {

    private final DatabaseHelper myDb;
    private final LayoutInflater inflater;
    private final Context context;

    // Constructor
    public CustomerCursorAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to, int flags, DatabaseHelper db) {
        super(context, cursor, flags);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.myDb = db;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate the customer_item layout
        return inflater.inflate(R.layout.customer_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Get references to the views in the customer_item layout
        TextView studentNumberTextView = view.findViewById(R.id.studentNumber);
        TextView accountNumberTextView = view.findViewById(R.id.accountNumber);
        TextView customerNameTextView = view.findViewById(R.id.customerName);
        TextView emailTextView = view.findViewById(R.id.email);
        TextView balanceTextView = view.findViewById(R.id.balance);

        // Get data from the cursor
        @SuppressLint("Range") String studentNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_2));
        @SuppressLint("Range") String accountNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_3));
        @SuppressLint("Range") String customerName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_6));
        @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_4));
        @SuppressLint("Range") String balance = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_5));

        // Bind the data to the views
        studentNumberTextView.setText("Student Number: " + studentNumber);
        accountNumberTextView.setText("Account Number: " + accountNumber);
        customerNameTextView.setText("Customer Name: " + customerName);
        emailTextView.setText("Email: " + email);
        balanceTextView.setText("Balance: R" + balance);

        // Get reference to buttons (delete, edit)
        Button deleteButton = view.findViewById(R.id.deleteButton);
        Button editButton = view.findViewById(R.id.editButton);

        // Check if the context is AdminActivity
        if (context instanceof AdminActivity) {
            // Show delete and edit buttons for AdminActivity
            deleteButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);

            // Get the customer ID
            @SuppressLint("Range") int customerId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_1));

            // Set click listener for the delete button
            deleteButton.setOnClickListener(v -> {
                // Delete customer from database
                myDb.deleteCustomer(customerId);

                // Update the cursor and refresh the adapter
                Cursor newCursor = myDb.getAllCustomers();
                changeCursor(newCursor); // Update adapter's cursor
                notifyDataSetChanged(); // Notify adapter to refresh the list
            });

            // Set click listener for the edit button
            editButton.setOnClickListener(v -> {
                // Create a bundle to pass the customer ID to the update screen
                Bundle args = new Bundle();
                args.putInt("customer_id", customerId); // Pass the customer ID to the next fragment

                // Navigate to UpdateCustomerFragment
                UpdateCustomerFragment editFragment = new UpdateCustomerFragment();
                editFragment.setArguments(args);

                // Navigate to the fragment for editing if the context is AdminActivity
                ((AdminActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, editFragment)
                        .addToBackStack(null)
                        .commit();
            });
        } else {
            // Hide delete and edit buttons for non-AdminActivity (e.g., FinancialAdvisor)
            deleteButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
        }
    }

}
