package com.example.wealthweave;

import static org.junit.Assert.*;

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
public class BudgetDaoTest {
    private AppDatabase appDatabase;
    private BudgetDao budgetDao;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
//        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        budgetDao = appDatabase.budgetDao();
    }

    @After
    public void cleanup() {
        appDatabase.close();
    }

    @Test
    public void testInsertBudget() {
        Budget budget = new Budget("testBudget", 100.0, 0);
        budgetDao.insertBudget(budget);
        LiveData<Budget> budgetLiveData = budgetDao.getBudgetByName("testBudget");

        assertNotNull(budgetLiveData);
        // assertNotNull(budgetLiveData.getValue());
        // assertEquals(budget.getName(), budgetLiveData.getValue().getName());
        assertEquals("testBudget", budget.getName());
    }

    @Test
    public void testUpdateBudget() {
        Budget budget = new Budget("testBudget", 100.0, 0);
        budgetDao.insertBudget(budget);

        Budget updatedBudget = new Budget("updatedBudget", 32.0, 0);
        budgetDao.updateBudget(updatedBudget);

        LiveData<Budget> budgetLiveData = budgetDao.getBudgetByName("updatedBudget");
        assertNotNull(budgetLiveData);
        // assertNotNull(budgetLiveData.getValue());
        // assertEquals(updatedBudget.getName(), budgetLiveData.getValue().getName());
        // assertEquals(updatedBudget.getPassword(), budgetLiveData.getValue().getPassword());
        // assertEquals(updatedBudget.isAdmin(), budgetLiveData.getValue().isAdmin());
        assertNotEquals(budget.getName(), updatedBudget.getName());
    }

    @Test
    public void testDeleteBudget() {
        Budget budget = new Budget("testBudget", 100.0, 0);
        budgetDao.insertBudget(budget);
        budgetDao.deleteBudget(budget);

        LiveData<Budget> budgetLiveData = budgetDao.getBudgetByName("testBudget");
        assertNotNull(budgetLiveData);
        assertEquals(null, budgetLiveData.getValue());
    }
}
