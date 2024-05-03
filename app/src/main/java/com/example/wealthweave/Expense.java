package com.example.wealthweave;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int expenseId;

    private String name;
    private Double amount;
    private int budgetId;
    private int userId;

    public Expense(String name, Double amount, int budgetId, int userId) {
        this.name = name;
        this.amount = amount;
        this.budgetId = budgetId;
        this.userId = userId;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "expenseId=" + expenseId +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", budgetId=" + budgetId +
                ", userId=" + userId +
                '}';
    }
}
