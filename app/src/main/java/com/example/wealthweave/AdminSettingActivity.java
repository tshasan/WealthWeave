package com.example.wealthweave;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.Executors;

public class AdminSettingActivity extends AppCompatActivity {

    private AppDatabase db;
    private ExpenseDao expenseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EditText deleteExpenseEditText;
        EditText deleteUserEditText;
        setContentView(R.layout.admin_setting_page);

        deleteUserEditText = findViewById(R.id.etDeleteUser);
        deleteExpenseEditText = findViewById(R.id.etDeleteExpense);
        Button deleteUserButton = findViewById(R.id.btnDeleteUser);
        Button deleteExpenseButton = findViewById(R.id.btnDeleteExpense);

        db = AppDatabase.getInstance(getApplicationContext());
        expenseDao = db.expenseDao();

        deleteUserButton.setOnClickListener(v -> {
            String username = deleteUserEditText.getText().toString();
            deleteUser(username);
        });

        deleteExpenseButton.setOnClickListener(v -> {
            String expenseName = deleteExpenseEditText.getText().toString();
            deleteExpense(expenseName);
        });
    }

    // OK this is giving problems but it works kinda idk how to fix this o well
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    private void deleteUser(final String username) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<User> users = db.userDao().getUsersByUsername(username);
            if (users != null && !users.isEmpty()) {
                for (User user : users) {
                    int userId = user.getUserId(); // Correctly retrieving the user ID.
                    db.runInTransaction(() -> {
                        db.userDao().deleteUser(user);
                        expenseDao.deleteExpensesByUserId(userId);
                    });
                }
                runOnUiThread(() -> Toast.makeText(AdminSettingActivity.this, "All users with username '" + username + "' deleted successfully", Toast.LENGTH_SHORT).show());
            } else {
                runOnUiThread(() -> Toast.makeText(AdminSettingActivity.this, "No users found with username: " + username, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void deleteExpense(final String expenseName) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Expense> expenses = db.expenseDao().getExpenseByName(expenseName);
            if (expenses != null && !expenses.isEmpty()) {
                for (Expense expense : expenses) {
                    db.expenseDao().deleteExpense(expense);
                }
                runOnUiThread(() -> Toast.makeText(AdminSettingActivity.this, "All expenses named '" + expenseName + "' removed successfully", Toast.LENGTH_SHORT).show());
            } else {
                runOnUiThread(() -> Toast.makeText(AdminSettingActivity.this, "No expenses found with name: " + expenseName, Toast.LENGTH_SHORT).show());
            }
        });
    }
}
