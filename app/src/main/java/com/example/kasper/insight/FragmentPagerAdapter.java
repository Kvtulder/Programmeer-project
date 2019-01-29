package com.example.kasper.insight;

import android.content.Intent;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private static final int NUM_OF_TABS = 2;

    FragmentItem incomeFragment = new FragmentItem();
    FragmentItem spendingFragment = new FragmentItem();

    public FragmentPagerAdapter(FragmentManager fm, PeriodObject period) {
        super(fm);

        // pass period object to the fragments
        Bundle incomeBundle = new Bundle();
        incomeBundle.putSerializable("period", period);
        incomeBundle.putBoolean("income", true);
        incomeFragment.setArguments(incomeBundle);


        Bundle spendingBundle = new Bundle();
        spendingBundle.putSerializable("period", period);
        incomeBundle.putBoolean("income", false);
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
        private StatisticsHelper helper = new StatisticsHelper();
        private TextView totalText;

        // set default to income fragment
        private Boolean income = Boolean.TRUE;

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
            if(arguments != null) {
                PeriodObject period = (PeriodObject) arguments.getSerializable("period");
                income = arguments.getBoolean("income");
                onPeriodChanged(period);
            }
            return view;
        }

        public void onPeriodChanged(PeriodObject period){


            if(income){
                transactions = sqlManager.getTransactionsByPeriod(period);
                totalText.setText("Inkomsten deze maand: " + helper.getTotal(transactions));
            }
            else{
                transactions = sqlManager.getTransactionsByPeriod(period);
                totalText.setText("Uitgaven deze maand: " + helper.getTotal(transactions));

            }

            // renew the piechart
            initPieChart();

        }

        // draw the pie chart
        public void initPieChart(){

            // create the dataset
            PieDataSet dataSet = helper.getSpendingPieData(transactions, "Insight");
            PieData data = new PieData(dataSet);
            pieChart.setData(data);

            // set the visuals
            pieChart.animateXY(1000, 100);
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            pieChart.setHoleRadius(45f);
            pieChart.setRotationEnabled(false);

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
