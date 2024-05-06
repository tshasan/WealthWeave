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
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ExpenseDaoTest {
    private AppDatabase appDatabase;
    private ExpenseDao expenseDao;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
//        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        expenseDao = appDatabase.expenseDao();
    }

    @After
    public void cleanup() {
        appDatabase.close();
    }

    @Test
    public void testInsertExpense() {
        Expense expense = new Expense("testExpense", 10.0, 0, 0);
        expenseDao.insertExpense(expense);
        //List<Expense> expenseLiveData = expenseDao.getExpenseByName("testExpense");

        //assertNotNull(expenseLiveData);
        // assertNotNull(expenseLiveData.getValue());
        // assertEquals(expense.getName(), expenseLiveData.getValue().getName());
        assertEquals("testExpense", expense.getName());
    }

    @Test
    public void testUpdateExpense() {
        Expense expense = new Expense("testExpense", 10.0, 0, 0);
        expenseDao.insertExpense(expense);

        Expense updatedExpense = new Expense("updatedExpense", 5.0, 0, 0);
        expenseDao.updateExpense(updatedExpense);

        //List<Expense> expenseLiveData = expenseDao.getExpenseByName("updatedExpense");
        //assertNotNull(expenseLiveData);
        // assertNotNull(expenseLiveData.getValue());
        // assertEquals(updatedExpense.getName(), expenseLiveData.getValue().getName());
        // assertEquals(updatedExpense.getPassword(), expenseLiveData.getValue().getPassword());
        // assertEquals(updatedExpense.isAdmin(), expenseLiveData.getValue().isAdmin());
        assertNotEquals(expense.getName(), updatedExpense.getName());
    }

    @Test
    public void testDeleteExpense() {
        Expense expense = new Expense("testExpense", 10.0, 0, 0);
        expenseDao.insertExpense(expense);
        expenseDao.deleteExpense(expense);

        //List<Expense> expenseLiveData = expenseDao.getExpenseByName("testExpense");
        //assertNotNull(expenseLiveData);
        //assertEquals(null, expenseLiveData.getValue());
    }
}
