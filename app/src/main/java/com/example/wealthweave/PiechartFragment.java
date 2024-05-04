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
        // Inflate the layout for this fragment
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
            // Each entry in the pie chart will be labeled with the user's ID or name
            entries.add(new PieEntry(sum.total, "User " + sum.userId));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense by User");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS); // Using vibrant colors for differentiation
        dataSet.setValueTextSize(12f); // Set the size of the text inside the pie chart
        dataSet.setValueTextColor(Color.WHITE); // Set the color of the value texts to white for better visibility

        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.setEntryLabelColor(Color.BLACK); // Set the color of entry labels (user IDs)
        pieChart.setEntryLabelTextSize(12f); // Set the size of the labels
        pieChart.getDescription().setEnabled(false); // Disable the description label on the chart
        pieChart.setUsePercentValues(true); // Use percentage values instead of raw data
        pieChart.setExtraOffsets(5, 10, 5, 5); // Adjust offsets to fit labels if needed
        pieChart.setCenterText("User Expenses Breakdown"); // Set a center text for the chart
        pieChart.setCenterTextSize(16f); // Set the size of the center text
        pieChart.animateY(1400, Easing.EaseInOutQuad); // Animate the pie chart on Y-axis

        pieChart.invalidate(); // Refreshes the pie chart with new data
    }

}
