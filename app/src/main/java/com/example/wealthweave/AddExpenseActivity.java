package com.example.wealthweave;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

public class AddExpenseActivity extends AppCompatActivity {
    private EditText expenseName, expenseAmount;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense_page);

        expenseName = findViewById(R.id.editTextExpenseName);
        expenseAmount = findViewById(R.id.editTextExpenseAmount);
        addButton = findViewById(R.id.buttonAddExpense);

        addButton.setOnClickListener(v -> addExpense());
    }

    private void addExpense() {
        String name = expenseName.getText().toString().trim();
        Double amount = Double.parseDouble(expenseAmount.getText().toString().trim());
        String username = LoginManager.getLoggedUsername(getApplicationContext());
        LiveData<User> user = AppDatabase.getInstance(getApplicationContext()).userDao().getUserByUsername(username);
        Integer userId = 0;
        if (user != null && user.getValue() != null) {
            userId = user.getValue().getUserId();
        }
        // TODO: budgetId
        Expense expense = new Expense(name, amount, 0, userId);

        Log.d("AddExpenseActivity", "Adding " + expense + " to budget");

        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            AppDatabase.getInstance(getApplicationContext()).expenseDao().insertExpense(expense);
        });
        finish();
    }
}