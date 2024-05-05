package com.example.wealthweave;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Expense.class, Budget.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);
    private static volatile AppDatabase INSTANCE;
    private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                UserDao userDao = INSTANCE.userDao();
                //insert default users
                userDao.insertUser(new User("testuser1", "testuser1", false));
                userDao.insertUser(new User("admin2", "admin2", true));
            });
        }
    };

    public static synchronized AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                    .addCallback(roomDatabaseCallback)
                    .build();
        }
        return INSTANCE;
    }

    public static ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }

    public abstract UserDao userDao();

    public abstract ExpenseDao expenseDao();

    public abstract BudgetDao budgetDao();
}
