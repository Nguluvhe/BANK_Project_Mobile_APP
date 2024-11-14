package com.nguluvhe_p.t_2020371775.ufsbankfinal;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FinancialAdvisorFragment extends Fragment {

    private DatabaseHelper myDb;
    private Cursor cursor;
    private CustomerCursorAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_financial_advisor, container, false);

        // Initialize DatabaseHelper
        myDb = new DatabaseHelper(getActivity());

        // Get reference to ListView
        ListView customerListViewF = view.findViewById(R.id.customerListViewF);

        // Fetch all customers from the database
        cursor = myDb.getAllCustomers();

        // Create and set the custom CursorAdapter
        adapter = new CustomerCursorAdapter(
                getActivity(),
                R.layout.financial_advisor_item,
                cursor,
                new String[]{DatabaseHelper.COL_2, DatabaseHelper.COL_3, DatabaseHelper.COL_6, DatabaseHelper.COL_4, DatabaseHelper.COL_5},
                new int[]{R.id.studentNumber, R.id.accountNumber, R.id.customerName, R.id.email, R.id.balance},
                0,
                myDb
        );

        // Set the adapter for the ListView
        customerListViewF.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close(); // Close the cursor when the fragment is destroyed
        }
    }
}
