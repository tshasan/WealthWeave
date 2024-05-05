package com.example.wealthweave;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PiechartFragment extends Fragment {
    private PieChart pieChart;
    private ExpenseViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expense_pie_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pieChart = view.findViewById(R.id.pieChart);
        setupPieChart();
    }

    private void setupPieChart() {
        viewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        viewModel.getExpenseSumByUser().observe(getViewLifecycleOwner(), this::updatePieChart);
    }

    private void updatePieChart(List<ExpenseDao.ExpenseUserSum> userSums) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (ExpenseDao.ExpenseUserSum sum : userSums) {
            entries.add(new PieEntry(sum.total, sum.username));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense by User");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setCenterText("User Expenses Breakdown");
        pieChart.setCenterTextSize(16f);
        pieChart.animateY(1400, Easing.EaseInOutQuad);

        pieChart.invalidate();
    }


}
