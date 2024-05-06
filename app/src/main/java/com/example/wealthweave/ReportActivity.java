package com.example.wealthweave;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_page);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new PiechartFragment())
                    .commit();
        }

        LiveData<List<Expense>> expensesLiveData = AppDatabase.getInstance(getApplicationContext()).expenseDao().getAllExpenses();
        expensesLiveData.observe(this, expenses -> {
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(ReportActivity.this));
            ExpenseAdapter adapter = new ExpenseAdapter(expenses);
            recyclerView.setAdapter(adapter);
        });


    }
}
