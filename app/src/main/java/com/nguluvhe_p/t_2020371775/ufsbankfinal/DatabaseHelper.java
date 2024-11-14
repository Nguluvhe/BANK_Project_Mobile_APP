package com.nguluvhe_p.t_2020371775.ufsbankfinal;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bankingDatabase.db";
    public static final String TABLE_NAME = "customers";

    // Table columns
    public static final String COL_1 = "_id"; // Primary key column
    public static final String COL_2 = "student_number";
    public static final String COL_3 = "account_number";
    public static final String COL_4 = "email";
    public static final String COL_5 = "balance";
    public static final String COL_6 = "customer_name";
    public static final String COL_7 = "address";
    public static final String COL_8 = "city";
    public static final String COL_9 = "postcode";
    public static final String COL_10 = "password";
    public static final String COL_11 = "role";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2); // Increment version to trigger onUpgrade
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // _id column
                COL_2 + " TEXT, " + // student_number
                COL_3 + " TEXT, " + // account_number
                COL_4 + " TEXT, " + // email
                COL_5 + " REAL, " + // balance
                COL_6 + " TEXT, " + // customer_name
                COL_7 + " TEXT, " + // address
                COL_8 + " TEXT, " + // city
                COL_9 + " TEXT, " + // postcode
                COL_10 + " TEXT, " + // password
                COL_11 + " TEXT" + // role
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert data with role (Customer or Admin)
    public boolean insertCustomer(String student_number, String account_number, String email, String balance,
                                  String customer_name, String address, String city, String postcode,
                                  String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM customers WHERE student_number = ?", new String[]{student_number});
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();

            if (count > 0) {
                return false;
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, student_number);
        contentValues.put(COL_3, account_number);
        contentValues.put(COL_4, email);
        contentValues.put(COL_5, balance);
        contentValues.put(COL_6, customer_name);
        contentValues.put(COL_7, address);
        contentValues.put(COL_8, city);
        contentValues.put(COL_9, postcode);
        contentValues.put(COL_10, password);
        contentValues.put(COL_11, role); // Role
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Check login by student number and password
    public String checkLogin(String student_number, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM " + TABLE_NAME +
                        " WHERE student_number=? AND password=?",
                new String[]{student_number, password});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(0); // Return the role (Customer or Admin)
        }
        return null; // Invalid login
    }

    public Cursor getAllCustomers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE role = 'Customer'", null);
    }

    // Method to fetch customer details by ID
    public Cursor getCustomerById(int customerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Query to fetch a customer with a specific ID
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE _id = ?", new String[]{String.valueOf(customerId)});
    }

    public int getCustomerIdByStudentNumber(String studentNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("customers", // Replace with your actual table name
                new String[]{"_id"}, // Replace with your actual customer ID column
                "student_number = ?", // Assuming "student_number" is the column name
                new String[]{studentNumber},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int customerId = cursor.getInt(cursor.getColumnIndex("_id")); // Replace with your actual column name
            cursor.close();
            return customerId;
        }
        if (cursor != null) {
            cursor.close();
        }
        return -1; // Return -1 if not found
    }



    public boolean updateCustomer(int customerId,String student_number, String account_number, String email, String balance,
                                  String customer_name, String address, String city, String postcode, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, student_number);
        contentValues.put(COL_3, account_number);
        contentValues.put(COL_4, email);
        contentValues.put(COL_5, balance);
        contentValues.put(COL_6, customer_name);
        contentValues.put(COL_7, address);
        contentValues.put(COL_8, city);
        contentValues.put(COL_9, postcode);
        contentValues.put(COL_10, password);
        long result = db.update(TABLE_NAME, contentValues, COL_1 + " = ?", new String[]{String.valueOf(customerId)});
        return result > 0;
    }

    public boolean deleteCustomer(int customerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_1 + " = ?", new String[]{String.valueOf(customerId)}) > 0;
    }

    @SuppressLint("Range")
    public boolean transferMoney(int senderId, String recipientAccountNumber, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Convert amount to double for calculations
        double transferAmount;
        try {
            transferAmount = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            return false; // Return false if the amount is not a valid number
        }

        double senderBalance = 0; // Declare senderBalance here

        // Get sender's current balance
        Cursor senderCursor = db.rawQuery("SELECT balance FROM customers WHERE _id = ?", new String[]{String.valueOf(senderId)});
        if (senderCursor != null && senderCursor.moveToFirst()) {
            senderBalance = senderCursor.getDouble(senderCursor.getColumnIndex("balance"));

            // Check if sender has enough balance
            if (senderBalance < transferAmount) {
                senderCursor.close();
                return false; // Not enough balance
            }
            senderCursor.close();
        } else {
            // If no sender found, close the cursor and return false
            if (senderCursor != null) {
                senderCursor.close();
            }
            return false; // Sender not found
        }

        // Get recipient's customer ID and balance
        Cursor recipientCursor = db.rawQuery("SELECT _id, balance FROM customers WHERE account_number = ?", new String[]{recipientAccountNumber});
        if (recipientCursor != null && recipientCursor.moveToFirst()) {
            @SuppressLint("Range") int recipientId = recipientCursor.getInt(recipientCursor.getColumnIndex("_id"));
            @SuppressLint("Range") double recipientBalance = recipientCursor.getDouble(recipientCursor.getColumnIndex("balance"));

            // Update balances
            double newSenderBalance = senderBalance - transferAmount;
            double newRecipientBalance = recipientBalance + transferAmount;

            // Begin transaction
            db.beginTransaction();
            try {
                // Update sender's balance
                db.execSQL("UPDATE customers SET balance = ? WHERE _id = ?", new Object[]{newSenderBalance, senderId});

                // Update recipient's balance
                db.execSQL("UPDATE customers SET balance = ? WHERE _id = ?", new Object[]{newRecipientBalance, recipientId});

                // Set transaction as successful
                db.setTransactionSuccessful();
                return true; // Transfer successful
            } finally {
                db.endTransaction(); // End transaction
            }
        } else {
            // If no recipient found, close the cursor and return false
            if (recipientCursor != null) {
                recipientCursor.close();
            }
            return false; // Recipient not found
        }
    }




    public void createTestData() {
        insertCustomer("2020123456", "ACC001", "admin@example.com", "1000.00",
                "Admin User", "123 Admin St", "Admin City", "0000", "admin123", "Admin");

        insertCustomer("2020345678", "ACC003", "consultant@example.com", "1000.00",
                "Consultant User", "123 Consultant St", "Consultant City", "0000", "consultant123", "Consultant");

        insertCustomer("2020901234", "ACC004", "financialadvisor@example.com", "1000.00",
                "FinancialAdvisor User", "123 Financial St", "Advisor City", "0000", "advisor123", "FinancialAdvisor");

        insertCustomer("2020789012", "ACC002", "customer@example.com", "500.00",
                "John Doe", "456 Customer St", "Customer City", "1234", "customer123", "Customer");
    }
}
