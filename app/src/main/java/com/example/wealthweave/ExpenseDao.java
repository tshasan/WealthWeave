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

    @Query("SELECT * FROM expenses WHERE expenseId = :expenseId")
    LiveData<Expense> getExpenseById(int expenseId);

    @Query("SELECT * FROM expenses WHERE name = :expenseName")
    LiveData<Expense> getExpenseByName(int expenseName);

    @Insert
    void insertExpense(Expense expense);

    @Update
    void updateExpense(Expense expense);

    @Delete
    void deleteExpense(Expense expense);
}
