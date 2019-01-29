package com.example.kasper.insight;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class InsightActivity extends AppCompatActivity {

    int CSVREADREQUESTCODE = 1;

    private ArrayList<TransactionObject> transactions;
    private FragmentPagerAdapter.FragmentItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_insight);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // set the adapters
        final Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(new DateSpinnerAdapter(this));


        PeriodObject period = (PeriodObject) spinner.getSelectedItem();
        final FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager(), period);
        final ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        // set up the tab listener
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            }
        };

        // create the two tabs
        actionBar.addTab(actionBar.newTab().setText("Inkomsten").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Uitgaven").setTabListener(tabListener));

        // create an reference the the open fragment
        int position = viewPager.getCurrentItem();
        item = (FragmentPagerAdapter.FragmentItem) adapter.getItem(position);

        // set an on item listener for the dropdown menu
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // update the UI
                PeriodObject period = (PeriodObject) spinner.getSelectedItem();
                InsightActivity.this.item.onPeriodChanged(period);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
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
            csvReader.storeTransactions(new CSVReader.CSVReaderCallback() {
                @Override
                public void onInvalidFile() {
                    Toast.makeText(InsightActivity.this, "Invalid file!", Toast.LENGTH_LONG);
                }

                @Override
                public void onParseException() {
                    Toast.makeText(InsightActivity.this, "Couldn't parse file!", Toast.LENGTH_LONG);
                }
            });

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Can't find selected file!", Toast.LENGTH_LONG).show();
        }

    }
}
