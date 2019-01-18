package com.example.kasper.insight;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class InsightActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insight);

        final Context context = this;

        final PieChart pieChart = findViewById(R.id.piechart);

       StatisticsHelper helper = new StatisticsHelper();
       final SQLManager sqlManager = SQLManager.getInstance(this);
       ArrayList<TransactionObject> transactions = sqlManager.getTransactions();

       PieDataSet dataSet = helper.getPieData(transactions, "Test");
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.animateXY(1000, 100);

        dataSet.setColors(ColorTemplate.PASTEL_COLORS);


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                final PieEntry pieEntry = (PieEntry) e;
                CategoryObject category = sqlManager.getCategoryByName(pieEntry.getLabel());
                Intent intent = new Intent(context, CategoryViewActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);

            }

            @Override
            public void onNothingSelected() {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // click listener for the toolbar menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        // loop over all the possibilities
        switch (item.getItemId()) {
            case R.id.action_download_transactions:
                intent = new Intent(this, CSVReaderActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_show_transactions:
                intent = new Intent(this, TransactionOverViewActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_show_categories:
                intent = new Intent(this, ManageCategoriesActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_process_transactions:
                intent = new Intent(this, ProcressTransactionsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
