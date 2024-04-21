package com.example.wealthweave;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button btnAddExpense, btnViewReport, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnViewReport = findViewById(R.id.btnViewReport);
        btnLogout = findViewById(R.id.btnLogout);

        Button btnAdminSettings = findViewById(R.id.btnAdminSettings);

        if (userIsAdmin(this)) {
            btnAdminSettings.setVisibility(View.VISIBLE); // Make the button visible if the user is an admin
        } else {
            btnAdminSettings.setVisibility(View.GONE); // Hide the button if the user is not an admin
        }


        setupListeners();
    }

    private void setupListeners() {
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //TODO implement functionality
                // Handle Add Expense
            }
        });

        btnViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //TODO implement functionality
                // Handle View Report
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private boolean userIsAdmin(Context context) {
        String userRole = LoginManager.getUserRole(context);
        return userRole.equalsIgnoreCase("admin");
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
