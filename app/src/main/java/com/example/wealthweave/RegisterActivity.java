package com.example.wealthweave;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private UserDao userDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        Button registerButton = findViewById(R.id.registerButton);

        userDao = AppDatabase.getInstance(getApplicationContext()).userDao();

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        userDao.countUsersByUsername(username).observe(this, count -> {
            if (count != null && count == 0) {
                // Username does not exist, proceed with registration
                User newUser = new User(username, password, false);
                AppDatabase.getDatabaseWriteExecutor().execute(() -> {
                    userDao.insertUser(newUser);
                    runOnUiThread(() -> {
                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close activity once registration is complete
                    });
                });
            } else {
                // Username exists
                Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

