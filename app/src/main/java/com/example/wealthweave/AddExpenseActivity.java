package com.example.wealthweave;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

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
        Expense expense = new Expense(name, amount, 0, 0);

        Log.d("AddExpenseActivity", "Adding " + expense + " to budget");

        AppDatabase.getInstance(getApplicationContext()).expenseDao().insertExpense(expense);
    }
}

//        // Accessing database using LiveData and observing the result
//        AppDatabase.getInstance(getApplicationContext()).userDao().login(username, password)
//                .observe(this, user -> {
//                    if (user != null) {
//                        handleSuccessfulLogin(username);
//                    } else {
//                        handleFailedLogin(username);
//                    }
//                });
//    }
//
//    private void handleSuccessfulLogin(String username) {
//        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//        SharedPreferences sharedPreferences = getSharedPreferences("WealthWeavePrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("isLoggedIn", true);
//        editor.putString("loggedUsername", username);
//        editor.apply();
//
//        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//        finish();
//    }
//
//    private void handleFailedLogin(String username) {
//        Log.d("LoginActivity", "Login failed for username: " + username); // Debug log
//        errorMessageTextView.setText("Invalid credentials. Please check your username and password.");
//        errorMessageTextView.setVisibility(View.VISIBLE);
//    }