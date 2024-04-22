package com.example.wealthweave;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText, confirmPasswordEditText;
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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if username exists using LiveData
        userDao.countUsersByUsername(username).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                if (count != null && count == 0) {
                    // Username does not exist, proceed with registration
                    User newUser = new User(username, password, false);  // Assume you have a constructor in User class
                    userDao.insertUser(newUser);
                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity once registration is complete
                } else {
                    // Username exists
                    Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

