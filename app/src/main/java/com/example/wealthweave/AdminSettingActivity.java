package com.example.wealthweave;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AdminSettingActivity extends AppCompatActivity {

    private EditText deleteUserEditText;
    private EditText deleteExpenseEditText;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_setting_page);
        deleteUserEditText = findViewById(R.id.etDeleteUser);
        deleteExpenseEditText = findViewById(R.id.etDeleteExpense);
        Button deleteUserButton = findViewById(R.id.btnDeleteUser);
        Button deleteExpenseButton = findViewById(R.id.btnDeleteExpense);

        db = AppDatabase.getInstance(getApplicationContext());

        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = deleteUserEditText.getText().toString();
                deleteUser(username);
            }
        });

        deleteExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expenseName = deleteExpenseEditText.getText().toString();
                deleteExpense(expenseName);
            }
        });
    }

    private void deleteUser(final String username) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> users = db.userDao().getUsersByUsername(username); // Assuming getUsersByUsername returns a List<User>
                if (users != null && !users.isEmpty()) {
                    for (User user : users) {
                        db.userDao().deleteUser(user);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AdminSettingActivity.this, "All users with username '" + username + "' deleted successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AdminSettingActivity.this, "No users found with username: " + username, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }


    private void deleteExpense(final String expenseName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Expense> expenses = db.expenseDao().getExpenseByName(expenseName); // Assuming getExpensesByName returns a List<Expense>
                if (expenses != null && !expenses.isEmpty()) {
                    for (Expense expense : expenses) {
                        db.expenseDao().deleteExpense(expense);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AdminSettingActivity.this, "All expenses named '" + expenseName + "' removed successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AdminSettingActivity.this, "No expenses found with name: " + expenseName, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
