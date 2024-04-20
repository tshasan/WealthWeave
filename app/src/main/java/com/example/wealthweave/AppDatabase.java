package com.example.wealthweave;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.Executors;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    private static volatile AppDatabase INSTANCE;

   public static synchronized AppDatabase getInstance(final Context context) {
       if (INSTANCE == null) {
           INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                   AppDatabase.class, "app_database")
                   .addCallback(roomDatabaseCallback)
                   .build();
       }
       return INSTANCE;
   }

   private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
       @Override
       public void onCreate(@NonNull SupportSQLiteDatabase db) {
           super.onCreate(db);
           Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
               @Override
               public void run() {
                   UserDao userDao = INSTANCE.userDao();
                   userDao.insertUser(new User("testuser1", "testuser1", false));
                   userDao.insertUser(new User("admin2", "admin2", true));
               }
           });
       }
   };
}