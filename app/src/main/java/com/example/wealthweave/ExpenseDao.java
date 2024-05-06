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
    List<Expense> getExpenseByName(String expenseName);

    @Query("SELECT * FROM expenses WHERE name = :name")
    LiveData<Expense> getExpenseByUserName(String name);

    @Query("SELECT budgetId, SUM(amount) AS total FROM expenses GROUP BY budgetId")
    LiveData<List<ExpenseSumByBudget>> getExpenseSumByBudget();

    @Query("SELECT e.userId, SUM(e.amount) AS total, u.username FROM expenses e JOIN users u ON e.userId = u.userId GROUP BY e.userId")
    LiveData<List<ExpenseUserSum>> getExpenseSumByUser();

    @Insert
    void insertExpense(Expense expense);

    @Update
    void updateExpense(Expense expense);

    @Query("DELETE FROM expenses WHERE userId = :userId")
    void deleteExpensesByUserId(int userId);

    @Delete
    void deleteExpense(Expense expense);

    public static class ExpenseSumByBudget {
        public int budgetId;
        public Float total;
    }

    public static class ExpenseUserSum {
        public String userId;
        public Float total;
        public String username;
    }

    public static class ExpenseCategorySum {
        public String category;
        public Float total;
    }
}
