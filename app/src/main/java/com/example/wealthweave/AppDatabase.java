package com.example.wealthweave;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Database(entities = {User.class, Expense.class, Budget.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase { // singleton implementation
    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService databaseWriteExecutor =
            new ThreadPoolExecutor(NUMBER_OF_THREADS, NUMBER_OF_THREADS,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
    private static volatile AppDatabase INSTANCE; // apprently this isnt thread safe but o well
    private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                try {
                    UserDao userDao = INSTANCE.userDao();
                    // insert default users
                    userDao.insertUser(new User("testuser1", "testuser1", false));
                    userDao.insertUser(new User("admin2", "admin2", true));
                } catch (Exception e) {
                    // Log or handle initialization errors here
                }
            });
        }
    };

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
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
