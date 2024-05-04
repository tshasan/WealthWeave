package com.example.wealthweave;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {
    private LiveData<List<ExpenseDao.ExpenseSumByBudget>> expenseSumByBudget;
    private LiveData<List<ExpenseDao.ExpenseUserSum>> expenseSumByUser;

    public ExpenseViewModel(Application application) {
        super(application);
        ExpenseDao expenseDao = AppDatabase.getInstance(application).expenseDao();
        expenseSumByBudget = expenseDao.getExpenseSumByBudget();
        expenseSumByUser = expenseDao.getExpenseSumByUser();
    }

    public LiveData<List<ExpenseDao.ExpenseSumByBudget>> getExpenseSumByBudget() {
        return expenseSumByBudget;
    }

    public LiveData<List<ExpenseDao.ExpenseUserSum>> getExpenseSumByUser() {
        return expenseSumByUser;
    }
}
