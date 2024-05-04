package com.example.wealthweave;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SettingActivity extends AppCompatActivity {
    private UserDao userDao;
    private User currentUser;  // Cache the current user data
    private ExecutorService executor = Executors.newSingleThreadExecutor();


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

        observeCurrentUser(); // Set up observation
    }

    private void observeCurrentUser() {
        String currentUsername = LoginManager.getLoggedUsername(this);
        if (currentUsername != null) {
            userDao.getUserByUsername(currentUsername).observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    currentUser = user; // Cache the current user data
                }
            });
        }
    }

    private void showDeleteUserDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_delete_account, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        AlertDialog dialog = dialogBuilder.create();

        Button confirmDeleteButton = dialogView.findViewById(R.id.btnConfirmDelete);
        Button cancelDeleteButton = dialogView.findViewById(R.id.btnCancelDelete);

        confirmDeleteButton.setOnClickListener(v -> handleDeleteUser(dialog));
        cancelDeleteButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void handleDeleteUser(AlertDialog dialog) {
        if (currentUser != null) {
            executor.submit(() -> {
                userDao.deleteUser(currentUser);
                LoginManager.logout(SettingActivity.this); // Logout user after deleting their account
                runOnUiThread(() -> {
                    Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });
            });
        }
    }


    private void showChangePasswordDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_change_password, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        AlertDialog dialog = dialogBuilder.create();

        Button confirmButton = dialogView.findViewById(R.id.btn_confirm);
        confirmButton.setOnClickListener(v -> handleChangePassword(dialogView, dialog));

        dialog.show();
    }

    private void handleChangePassword(View dialogView, AlertDialog dialog) {
        EditText oldPassword = dialogView.findViewById(R.id.old_password);
        EditText newPassword = dialogView.findViewById(R.id.new_password);
        EditText confirmNewPassword = dialogView.findViewById(R.id.confirm_new_password);

        String oldPwdText = oldPassword.getText().toString();
        String newPwdText = newPassword.getText().toString();
        String confirmPwdText = confirmNewPassword.getText().toString();

        if (!newPwdText.equals(confirmPwdText)) {
            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUser == null || !currentUser.getPassword().equals(oldPwdText)) {
            Toast.makeText(this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
            return;
        }

        // Submit the database update operation to the executor
        executor.submit(() -> {
            userDao.updatePassword(currentUser.getUsername(), newPwdText);

            // Update UI on the main thread
            runOnUiThread(() -> {
                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });
    }

}
// TODO EXPORT just print something idk
// TODO THEME CHANGE change theme based on android 
