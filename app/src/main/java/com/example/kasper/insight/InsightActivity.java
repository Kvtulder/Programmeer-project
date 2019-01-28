package com.example.kasper.insight;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;

// import all the piechart classes
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;


public class InsightActivity extends AppCompatActivity {


    int CSVREADREQUESTCODE = 1;

    // init variables
    private PieChart pieChart;
    private Context context;
    private SQLManager sqlManager;
    private StatisticsHelper helper = new StatisticsHelper();

    private ArrayList<TransactionObject> transactions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insight);

        // init sql manager
        sqlManager = SQLManager.getInstance(this);

        pieChart = findViewById(R.id.piechart);
        context = this;

        // set the adapters
        final Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(new DateSpinnerAdapter(this));

        // renew the transaction list to the selected period
        PeriodObject period = (PeriodObject) spinner.getSelectedItem();
        onPeriodChanged(period);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // update the UI
                PeriodObject period = (PeriodObject) spinner.getSelectedItem();
                onPeriodChanged(period);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void onPeriodChanged(PeriodObject period){

        transactions = sqlManager.getTransactionsByPeriod(period);

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

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            // guide the user to the category activity when clicked on a piechart category
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
                // never gets called because we start a new activity when a value is selected
                //
            }
        });

    }

    // inflate our custom toolbar menu
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
                intent = new Intent().setType("text/csv")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Selecteer een CSV bestand"), CSVREADREQUESTCODE);
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

    // set listener for the file selector
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if(data == null){
                Toast.makeText(this,"Geen bestand gevonden", Toast.LENGTH_LONG).show();
                return;
            }
            FileDescriptor fd = getContentResolver()
                    .openFileDescriptor(data.getData(), "r").getFileDescriptor();

            CSVReader csvReader = new CSVReader(fd, this);
            csvReader.storeTransactions(new CSVReaderCallback() {
                @Override
                public void onInvalidFile() {
                    Toast.makeText(context, "Invalid file!", Toast.LENGTH_LONG);
                }

                @Override
                public void onParseException() {
                    Toast.makeText(context, "Couldn't parse file!", Toast.LENGTH_LONG);
                }
            });

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Can't find selected file!", Toast.LENGTH_LONG).show();
        }

    }
}
