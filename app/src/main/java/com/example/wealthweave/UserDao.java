package com.example.wealthweave;

import androidx.room.*;
import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("SELECT * FROM users WHERE userId = :userId")
    User getUserById(int userId);

    @Query("SELECT * FROM users WHERE username = :username")
    User getUserByUsername(String username);

    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    User login(String username, String password);

    @Query("SELECT COUNT(userId) FROM users WHERE username = :username")
    int countUsersByUsername(String username);
}