package com.example.wealthweave;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddExpenseActivity extends AppCompatActivity {
    private EditText expenseName;
    private EditText expenseAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button addButton;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense_page);

        expenseName = findViewById(R.id.editTextExpenseName);
        expenseAmount = findViewById(R.id.editTextExpenseAmount);
        addButton = findViewById(R.id.buttonAddExpense);

        addButton.setOnClickListener(v -> attemptAddExpense());
    }

    private void attemptAddExpense() {
        String name = expenseName.getText().toString().trim();
        double amount = parseExpenseAmount();
        if (amount == -1) {
            Toast.makeText(this, "Invalid amount. Please enter a valid number.", Toast.LENGTH_LONG).show();
            return;
        }
        fetchUserAndAddExpense(name, amount);
    }

    private double parseExpenseAmount() {
        try {
            return Double.parseDouble(expenseAmount.getText().toString().trim());
        } catch (NumberFormatException e) {
            Log.e("AddExpenseActivity", "Error parsing amount: " + e.getMessage());
            return -1;
        }
    }

    private void fetchUserAndAddExpense(String expenseName, double amount) {
        String username = LoginManager.getLoggedUsername(getApplicationContext());
        AppDatabase.getInstance(getApplicationContext()).userDao().getUserByUsername(username).observe(this, user -> {
            if (user != null) {
                int userId = user.getUserId();
                addExpenseToDatabase(expenseName, amount, userId);
            } else {
                Toast.makeText(this, "User not found.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addExpenseToDatabase(String name, double amount, int userId) {
        Expense expense = new Expense(name, amount, 0, userId);  // Assuming budgetId is 0 for now
        Log.d("AddExpenseActivity", "Adding " + expense + " to budget");
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            AppDatabase.getInstance(getApplicationContext()).expenseDao().insertExpense(expense);
            runOnUiThread(this::finish);
        });
    }
}
