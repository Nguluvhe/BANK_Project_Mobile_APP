package com.nguluvhe_p.t_2020371775.ufsbankfinal;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.fragment.app.Fragment;

public class ViewCustomersFragment extends Fragment {

    DatabaseHelper myDb;
    Cursor cursor;
    CustomerCursorAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_customers, container, false);
        myDb = new DatabaseHelper(getActivity());

        // Get reference to ListView
        ListView customerListView = view.findViewById(R.id.customerListView);

        // Fetch all customers
        cursor = myDb.getAllCustomers();

        // Create and set the custom adapter
        adapter = new CustomerCursorAdapter(
                getActivity(),
                R.layout.customer_item,
                cursor,
                new String[]{DatabaseHelper.COL_2, DatabaseHelper.COL_3, DatabaseHelper.COL_6, DatabaseHelper.COL_4, DatabaseHelper.COL_5},
                new int[]{R.id.studentNumber, R.id.accountNumber, R.id.customerName, R.id.email, R.id.balance},
                0,
                myDb
        );

        customerListView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cursor != null) {
            cursor.close(); // Close the cursor when done
        }
    }
}


