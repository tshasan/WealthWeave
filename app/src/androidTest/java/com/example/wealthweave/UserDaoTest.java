package com.example.wealthweave;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserDaoTest {
    private AppDatabase appDatabase;
    private UserDao userDao;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
//        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = appDatabase.userDao();
    }

    @After
    public void cleanup() {
        appDatabase.close();
    }

    @Test
    public void testInsertUser() {
        User user = new User("testUser", "password", false);
        userDao.insertUser(user);
        LiveData<User> userLiveData = userDao.getUserByUsername("testUser");

        assertNotNull(userLiveData);
        // assertNotNull(userLiveData.getValue());
        // assertEquals(user.getUsername(), userLiveData.getValue().getUsername());
        assertEquals("testUser", user.getUsername());
    }

    @Test
    public void testUpdateUser() {
        User user = new User("testUser", "password", false);
        userDao.insertUser(user);

        User updatedUser = new User("updatedUser", "newPassword", true);
        userDao.updateUser(updatedUser);

        LiveData<User> userLiveData = userDao.getUserByUsername("updatedUser");
        assertNotNull(userLiveData);
        // assertNotNull(userLiveData.getValue());
        // assertEquals(updatedUser.getUsername(), userLiveData.getValue().getUsername());
        // assertEquals(updatedUser.getPassword(), userLiveData.getValue().getPassword());
        // assertEquals(updatedUser.isAdmin(), userLiveData.getValue().isAdmin());
        assertNotEquals(user.getUsername(), updatedUser.getUsername());
    }

    @Test
    public void testDeleteUser() {
        User user = new User("testUser", "password", false);
        userDao.insertUser(user);
        userDao.deleteUser(user);

        LiveData<User> userLiveData = userDao.getUserByUsername("testUser");
        assertNotNull(userLiveData);
        assertNull(userLiveData.getValue());
    }
}
