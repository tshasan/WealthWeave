package com.example.wealthweave;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM users WHERE userId = :userId")
    LiveData<User> getUserById(int userId);

    @Query("SELECT * FROM users WHERE username = :username")
    LiveData<User> getUserByUsername(String username);

    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Query("UPDATE users SET password = :newPassword WHERE username = :username")
    void updatePassword(String username, String newPassword);


    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    LiveData<User> login(String username, String password);

    @Query("SELECT COUNT(userId) FROM users WHERE username = :username")
    LiveData<Integer> countUsersByUsername(String username);

    @Query("SELECT isAdmin FROM users WHERE username = :username")
    LiveData<Boolean> isAdmin(String username);
}
