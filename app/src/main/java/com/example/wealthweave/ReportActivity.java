package com.example.wealthweave;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
    }
}
