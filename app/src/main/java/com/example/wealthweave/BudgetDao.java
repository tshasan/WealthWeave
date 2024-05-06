package com.example.wealthweave;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface BudgetDao {
    @Query("SELECT * FROM budgets WHERE budgetId = :budgetId")
    LiveData<Budget> getBudgetById(int budgetId);

    @Query("SELECT * FROM budgets WHERE name = :budgetName")
    LiveData<Budget> getBudgetByName(String budgetName);

    @Insert
    void insertBudget(Budget budget);

    @Update
    void updateBudget(Budget budget);

    @Delete
    void deleteBudget(Budget budget);
}