package com.nguluvhe_p.t_2020371775.ufsbankfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class AddCustomerFragment extends Fragment {

    DatabaseHelper myDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_customer, container, false);
        myDb = new DatabaseHelper(getActivity());

        EditText studentNumberInput = view.findViewById(R.id.studentNumberInput);
        EditText accountNumberInput = view.findViewById(R.id.accountNumberInput);
        EditText emailInput = view.findViewById(R.id.emailInput);
        EditText balanceInput = view.findViewById(R.id.balanceInput);
        EditText nameInput = view.findViewById(R.id.nameInput);
        EditText addressInput = view.findViewById(R.id.addressInput);
        EditText cityInput = view.findViewById(R.id.cityInput);
        EditText postcodeInput = view.findViewById(R.id.postcodeInput);
        EditText passwordInput = view.findViewById(R.id.passwordInput);
        Button addCustomerBtn = view.findViewById(R.id.addCustomerBtn);

        addCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentNumber = studentNumberInput.getText().toString();
                String accountNumber = accountNumberInput.getText().toString();
                String email = emailInput.getText().toString();
                String balance = balanceInput.getText().toString();
                String name = nameInput.getText().toString();
                String address = addressInput.getText().toString();
                String city = cityInput.getText().toString();
                String postcode = postcodeInput.getText().toString();
                String password = passwordInput.getText().toString();

                boolean result = myDb.insertCustomer(studentNumber, accountNumber, email, balance, name, address, city, postcode, password, "Customer");
                if (result) {
                    Toast.makeText(getActivity(), "Customer added successfully!", Toast.LENGTH_SHORT).show();
                    ViewCustomersFragment viewCustomerFragment = new ViewCustomersFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, viewCustomerFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Toast.makeText(getActivity(), "Failed to add customer", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
