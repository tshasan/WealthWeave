package com.example.wealthweave;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 1000; // Delay time in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page); // Layout for the splash screen

        new Handler().postDelayed(this::checkLogin, SPLASH_DISPLAY_LENGTH);
    }

    private void checkLogin() {
        if (!LoginManager.isLoggedIn(this)) {
            // User is not logged in, redirect to LoginActivity
            startNewActivity(LoginActivity.class);
        } else {
            // User is logged in, redirect to HomePageActivity
            startNewActivity(HomeActivity.class);
        }
    }

    private void startNewActivity(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
        finish();
    }
}

