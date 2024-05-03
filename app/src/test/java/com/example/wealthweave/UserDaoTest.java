import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.wealthweave.AppDatabase;
import com.example.wealthweave.User;
import com.example.wealthweave.UserDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

public class UserDaoTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private AppDatabase appDatabase;
    private UserDao userDao;

    @Before
    public void createDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                        AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        userDao = appDatabase.userDao();
    }

    @After
    public void closeDb() {
        appDatabase.close();
    }

    @Test
    public void testInsertUser() throws InterruptedException {
        User user = new User("username", "password", false);
        userDao.insertUser(user);

        User liveDataUser = getValue(userDao.getUserByUsername("username"));
        assertNotNull(liveDataUser);
        assertEquals("username", liveDataUser.getUsername());
    }

    @Test
    public void testGetAllUsers() throws InterruptedException {
        User user1 = new User("user1", "password1", false);
        User user2 = new User("user2", "password2", false);
        userDao.insertUser(user1);
        userDao.insertUser(user2);

        List<User> liveDataUsers = getValue(userDao.getAllUsers());
        assertNotNull(liveDataUsers);
        assertEquals(2, liveDataUsers.size());
    }

    // Helper method to get the value from LiveData
    private <T> T getValue(LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        // Observer to set the value and then immediately remove itself
        final Object lock = new Object();
        liveData.observeForever(o -> {
            data[0] = o;
            synchronized (lock) {
                lock.notify();
            }
        });
        // Waiting for data to be set
        synchronized (lock) {
            while (data[0] == null) {
                lock.wait();
            }
        }
        //noinspection unchecked
        return (T) data[0];
    }
}
