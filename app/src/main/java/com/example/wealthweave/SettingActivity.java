package com.example.wealthweave;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SettingActivity extends AppCompatActivity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private UserDao userDao;
    private User currentUser;
    private Button btnAdminSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page);

        AppDatabase database = AppDatabase.getInstance(this);
        userDao = database.userDao();

        Button changePasswordButton = findViewById(R.id.btn_change_password);
        changePasswordButton.setOnClickListener(v -> showChangePasswordDialog());

        Button deleteAccountButton = findViewById(R.id.btn_delete_account);
        deleteAccountButton.setOnClickListener(v -> showDeleteUserDialog());

        btnAdminSetting = findViewById(R.id.btn_admin_settings);
        btnAdminSetting.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminSettingActivity.class));
        });
        checkAdminStatus();
        observeCurrentUser();
    }

    private void checkAdminStatus() {
        SharedPreferences prefs = getSharedPreferences("WealthWeavePrefs", MODE_PRIVATE);
        String currentUsername = prefs.getString("loggedUsername", "");

        LiveData<Boolean> isAdminLiveData = userDao.isAdmin(currentUsername);
        isAdminLiveData.observe(this, isAdmin -> {
            btnAdminSetting.setVisibility(isAdmin != null && isAdmin ? View.VISIBLE : View.GONE);
        });
    }

    private void observeCurrentUser() {
        String currentUsername = LoginManager.getLoggedUsername(this);
        if (currentUsername != null) {
            userDao.getUserByUsername(currentUsername).observe(this, user -> {
                currentUser = user; // Cache the current user data
            });
        }
    }

    private void showDeleteUserDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_delete_account, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        dialogView.findViewById(R.id.btnConfirmDelete).setOnClickListener(v -> handleDeleteUser(dialog));
        dialogView.findViewById(R.id.btnCancelDelete).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void handleDeleteUser(AlertDialog dialog) {
        if (currentUser != null) {
            executor.submit(() -> {
                userDao.deleteUser(currentUser);
                LoginManager.logout(this);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    startActivity(new Intent(this, MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                });
            });
        }
    }

    private void showChangePasswordDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_change_password, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        dialogView.findViewById(R.id.btn_confirm).setOnClickListener(v -> handleChangePassword(dialogView, dialog));

        dialog.show();
    }

    private void handleChangePassword(View dialogView, AlertDialog dialog) {
        EditText oldPassword = dialogView.findViewById(R.id.old_password);
        EditText newPassword = dialogView.findViewById(R.id.new_password);
        EditText confirmNewPassword = dialogView.findViewById(R.id.confirm_new_password);

        if (!newPassword.getText().toString().equals(confirmNewPassword.getText().toString())) {
            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUser == null || !currentUser.getPassword().equals(oldPassword.getText().toString())) {
            Toast.makeText(this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
            return;
        }

        executor.submit(() -> {
            userDao.updatePassword(currentUser.getUsername(), newPassword.getText().toString());
            runOnUiThread(() -> {
                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });
    }
}
