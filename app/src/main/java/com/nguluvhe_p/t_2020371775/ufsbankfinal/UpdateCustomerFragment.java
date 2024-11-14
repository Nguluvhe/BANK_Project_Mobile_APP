package com.nguluvhe_p.t_2020371775.ufsbankfinal;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class UpdateCustomerFragment extends Fragment {

    DatabaseHelper myDb;
    int customerId;

    EditText updateStudentNumberInput, updateAccountNumberInput, updateEmailInput, updateBalanceInput,
            updateNameInput, updateAddressInput, updateCityInput, updatePostcodeInput, updatePasswordInput;
    Button updateCustomerBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_customer, container, false);

        myDb = new DatabaseHelper(getActivity());

        // Get the customer ID from the arguments passed by the previous fragment
        if (getArguments() != null) {
            customerId = getArguments().getInt("customer_id");
        }

        // Initialize the EditText fields
        updateStudentNumberInput = view.findViewById(R.id.updateStudentNumberInput);
        updateAccountNumberInput = view.findViewById(R.id.updateAccountNumberInput);
        updateEmailInput = view.findViewById(R.id.updateEmailInput);
        updateBalanceInput = view.findViewById(R.id.updateBalanceInput);
        updateNameInput = view.findViewById(R.id.updateNameInput);
        updateAddressInput = view.findViewById(R.id.updateAddressInput);
        updateCityInput = view.findViewById(R.id.updateCityInput);
        updatePostcodeInput = view.findViewById(R.id.updatePostcodeInput);
        updatePasswordInput = view.findViewById(R.id.updatePasswordInput);
        updateCustomerBtn = view.findViewById(R.id.updateCustomerBtn);

        // Populate the fields with existing customer data
        populateCustomerData(customerId);

        // Set the listener for the Update button
        updateCustomerBtn.setOnClickListener(v -> {
            // Call method to update customer
            updateCustomerDetails();
        });

        return view;
    }

    // Method to populate customer data into form fields
    @SuppressLint("Range")
    private void populateCustomerData(int customerId) {
        Cursor cursor = myDb.getCustomerById(customerId);
        if (cursor != null && cursor.moveToFirst()) {
            updateStudentNumberInput.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_2)));
            updateAccountNumberInput.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_3)));
            updateEmailInput.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_4)));
            updateBalanceInput.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_5)));
            updateNameInput.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_6)));
            updateAddressInput.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_7)));
            updateCityInput.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_8)));
            updatePostcodeInput.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_9)));
            updatePasswordInput.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_10)));
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    // Method to update customer details
    private void updateCustomerDetails() {
        boolean isUpdated = myDb.updateCustomer(
                customerId, // Pass the customer ID here
                updateStudentNumberInput.getText().toString(),
                updateAccountNumberInput.getText().toString(),
                updateEmailInput.getText().toString(),
                updateBalanceInput.getText().toString(),
                updateNameInput.getText().toString(),
                updateAddressInput.getText().toString(),
                updateCityInput.getText().toString(),
                updatePostcodeInput.getText().toString(),
                updatePasswordInput.getText().toString());

        if (isUpdated) {
            // Show success message and navigate back to the customer list
            Toast.makeText(getActivity(), "Customer updated successfully", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            // Show failure message
            Toast.makeText(getActivity(), "Failed to update customer", Toast.LENGTH_SHORT).show();
        }
    }

}

