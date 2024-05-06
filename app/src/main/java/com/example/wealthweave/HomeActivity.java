package com.example.wealthweave;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button btnAddExpense;
    private Button btnViewReport;
    private Button btnLogout;
    private Button btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnViewReport = findViewById(R.id.btnViewReport);
        btnLogout = findViewById(R.id.btnLogout);
        btnSettings = findViewById(R.id.btnSettings);

        LiveData<List<Expense>> expensesLiveData = AppDatabase.getInstance(getApplicationContext()).expenseDao().getAllExpenses();
        expensesLiveData.observe(this, expenses -> {
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
            ExpenseAdapter adapter = new ExpenseAdapter(expenses);
            recyclerView.setAdapter(adapter);
        });

        setupListeners();
    }

    private void setupListeners() {
        btnAddExpense.setOnClickListener(v -> startActivity(new Intent(this, AddExpenseActivity.class)));

        btnViewReport.setOnClickListener(v -> startActivity(new Intent(this, ReportActivity.class)));

        btnLogout.setOnClickListener(v -> logoutUser());

        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingActivity.class));
            finish();
        });
    }

    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("WealthWeavePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.remove("loggedUsername");
        editor.apply();

        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
