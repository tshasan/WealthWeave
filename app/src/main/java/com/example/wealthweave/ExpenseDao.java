package com.example.wealthweave;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Query("SELECT * FROM expenses")
    LiveData<List<Expense>> getAllExpenses();

    @Query("SELECT * FROM expenses WHERE budgetID = :budgetId")
    LiveData<List<Expense>> getAllExpenseFromBudget(int budgetId);

    @Query("SELECT * FROM expenses WHERE expenseId = :expenseId")
    LiveData<Expense> getExpenseById(int expenseId);

    @Query("SELECT * FROM expenses WHERE name = :expenseName")
    LiveData<Expense> getExpenseByName(String expenseName);

    @Query("SELECT budgetId, SUM(amount) AS total FROM expenses GROUP BY budgetId")
    LiveData<List<ExpenseSumByBudget>> getExpenseSumByBudget();

    @Query("SELECT userId, SUM(amount) AS total FROM expenses GROUP BY userId")
    LiveData<List<ExpenseUserSum>> getExpenseSumByUser();


    @Insert
    void insertExpense(Expense expense);

    @Update
    void updateExpense(Expense expense);

    @Delete
    void deleteExpense(Expense expense);

    public static class ExpenseSumByBudget {
        public int budgetId;
        public Float total;
    }

    public static class ExpenseUserSum {
        public String userId;
        public Float total;
    }

    public static class ExpenseCategorySum {
        public String category;
        public Float total;
    }

}
