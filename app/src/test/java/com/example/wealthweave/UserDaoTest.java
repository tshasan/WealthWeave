package com.example.wealthweave;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// TODO: DOES NOT WORK!!

public class UserDaoTest {

    private AppDatabase appDatabase;
    private UserDao userDao;

    @Before
    public void createDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        userDao = appDatabase.userDao();
    }

    @After
    public void closeDb() {
        appDatabase.close();
    }

    @Test
    public void testInsertUser() {
        User user = new User("testUser", "password", false);
        userDao.insertUser(user);
        LiveData<User> userLiveData = userDao.getUserByUsername("testUser");
        assertNotNull(userLiveData);
        assertEquals(user.getUsername(), userLiveData.getValue().getUsername());
    }

    @Test
    public void testUpdateUser() {
        User user = new User("testUser", "password", false);
        userDao.insertUser(user);

        User updatedUser = new User("updatedUser", "newPassword", true);
        userDao.updateUser(updatedUser);

        LiveData<User> userLiveData = userDao.getUserByUsername("updatedUser");
        assertNotNull(userLiveData);
        assertEquals(updatedUser.getUsername(), userLiveData.getValue().getUsername());
        assertEquals(updatedUser.getPassword(), userLiveData.getValue().getPassword());
        assertEquals(updatedUser.isAdmin(), userLiveData.getValue().isAdmin());
    }

    @Test
    public void testDeleteUser() {
        User user = new User("testUser", "password", false);
        userDao.insertUser(user);
        userDao.deleteUser(user);

        LiveData<User> userLiveData = userDao.getUserByUsername("testUser");
        assertNotNull(userLiveData);
        assertEquals(null, userLiveData.getValue());
    }
}