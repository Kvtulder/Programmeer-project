package com.example.kasper.insight;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class InsightTab extends android.support.v4.app.FragmentPagerAdapter {

    private static final int NUM_OF_TABS = 2;

    FragmentItem incomeFragment = new FragmentItem();
    FragmentItem spendingFragment = new FragmentItem();

    public InsightTab(FragmentManager fm, PeriodObject period) {
        super(fm);

        // pass period object to the fragments
        Bundle incomeBundle = new Bundle();
        incomeBundle.putSerializable("period", period);
        incomeBundle.putBoolean("income", Boolean.TRUE);
        incomeFragment.setArguments(incomeBundle);

        Bundle spendingBundle = new Bundle();
        spendingBundle.putSerializable("period", period);
        spendingBundle.putBoolean("income", Boolean.FALSE);
        spendingFragment.setArguments(spendingBundle);
    }

    @Override
    public Fragment getItem(int i) {
        if(i == 0)
            return incomeFragment;
        else
            return spendingFragment;
    }

    @Override
    public int getCount() {
        return NUM_OF_TABS;
    }

    public static class FragmentItem extends Fragment{

        // init variables
        private PieChart pieChart;
        private SQLManager sqlManager;
        private StatisticsHelper helper;
        private TextView totalText;

        // set default to income fragment
        private Boolean income;
        private ArrayList<TransactionObject> transactions;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.insight_tab_item, container, false);

            // init sql manager
            sqlManager = SQLManager.getInstance(getContext());
            pieChart = view.findViewById(R.id.piechart);
            totalText = view.findViewById(R.id.totalText);

            // renew the transaction list to the selected period
            Bundle arguments = getArguments();
            if(arguments == null)
                return view;


            PeriodObject period = (PeriodObject) arguments.getSerializable("period");
            income = arguments.getBoolean("income");
            onPeriodChanged(period);
            return view;
        }

        public void onPeriodChanged(PeriodObject period){
            transactions = sqlManager.getTransactionsByPeriod(period);
            helper = new StatisticsHelper(transactions, !income, income);

            if(income)
                totalText.setText(String.format("Inkomsten deze maand: %.2f", helper.getTotal()));
            else
                totalText.setText(String.format("Uitgaven deze maand: %.2f", helper.getTotal() * -1));
            // renew the piechart
            initPieChart(income);
        }

        // draw the pie chart
        public void initPieChart(boolean income){

            // create the dataset
            PieDataSet dataSet = helper.getPieData("");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            PieData data = new PieData(dataSet);
            data.setValueTextSize(14);
            data.setValueTextColor(Color.WHITE);
            pieChart.setData(data);

            // set the visuals
            pieChart.animateXY(1000, 100);
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            pieChart.setHoleRadius(45f);
            pieChart.setRotationEnabled(false);
            Description description = new Description();
            description.setEnabled(false);
            pieChart.setDescription(description);
            pieChart.setUsePercentValues(true);
            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                // guide the user to the category activity when clicked on a piechart category
                @Override
                public void onValueSelected(Entry e, Highlight h) {

                    final PieEntry pieEntry = (PieEntry) e;
                    CategoryObject category = sqlManager.getCategoryByName(pieEntry.getLabel());
                    Intent intent = new Intent(getContext(), CategoryViewActivity.class);
                    intent.putExtra("category", category);
                    startActivity(intent);
                }

                @Override
                public void onNothingSelected() {
                    // never gets called because we start a new activity when a value is selected
                }
            });
        }
    }
}
