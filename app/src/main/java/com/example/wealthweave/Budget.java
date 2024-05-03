package com.example.wealthweave;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "budgets")
public class Budget {
    @PrimaryKey(autoGenerate = true)
    private int budgetId;

    private String name;
    private Double limitAmount;
    private int userId;

    public Budget(String name, Double limitAmount, int userId) {
        this.name = name;
        this.limitAmount = limitAmount;
        this.userId = userId;
    }

    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLimitAmount() {
        return limitAmount;
    }


    public void setLimitAmount(Double limitAmount) {
        this.limitAmount = limitAmount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "budgetId=" + budgetId +
                ", name='" + name + '\'' +
                ", limitAmount=" + limitAmount +
                ", userId=" + userId +
                '}';
    }
}
