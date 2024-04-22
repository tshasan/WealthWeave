package com.example.wealthweave;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;
    private TextView errorMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        errorMessageTextView = findViewById(R.id.errorMessageTextView);

        loginButton.setOnClickListener(v -> loginUser());
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = hashPassword(passwordEditText.getText().toString().trim());

        Log.d("LoginActivity", "Attempting to login with username: " + username); // Debug log

        // Accessing database using LiveData and observing the result
        AppDatabase.getInstance(getApplicationContext()).userDao().login(username, password)
                .observe(this, user -> {
                    if (user != null) {
                        handleSuccessfulLogin(username);
                    } else {
                        handleFailedLogin(username);
                    }
                });
    }

    private void handleSuccessfulLogin(String username) {
        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences("WealthWeavePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("loggedUsername", username);
        editor.apply();

        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }

    private void handleFailedLogin(String username) {
        Log.d("LoginActivity", "Login failed for username: " + username); // Debug log
        errorMessageTextView.setText("Invalid credentials. Please check your username and password.");
        errorMessageTextView.setVisibility(View.VISIBLE);
    }

    private String hashPassword(String password) {
        Log.d("LoginActivity", "Password : " + password);
        return password;
    }

}

