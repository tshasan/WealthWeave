package com.example.wealthweave;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        final String username = usernameEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        // Using ExecutorService to handle database operation off the UI thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            User user = AppDatabase.getInstance(getApplicationContext()).userDao().login(username, password);
            // Update UI on the main thread
            runOnUiThread(() -> {
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    // Save login status to SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("WealthWeavePrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("loggedUsername", username); // this should saver the logged in users username
                    editor.apply();

                    // Navigate to Home page
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish(); // Close the LoginActivity
                } else {
                    errorMessageTextView.setText("Invalid credentials");
                    errorMessageTextView.setVisibility(View.VISIBLE);
                }
            });
        });
    }
}
