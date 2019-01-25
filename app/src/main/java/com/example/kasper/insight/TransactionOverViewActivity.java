package com.example.kasper.insight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class TransactionOverViewActivity extends AppCompatActivity {

    private TransactionListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_over_view);
        final Context context = this;

        SQLManager sqlManager = SQLManager.getInstance(this);
        final ArrayList<TransactionObject> transactions = sqlManager.getTransactions();

        listAdapter = new TransactionListAdapter(this, transactions);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, TransactionViewActivity.class);

                TransactionObject transaction = (TransactionObject) listAdapter.getItem(position);
                intent.putExtra("transaction", transaction);
                startActivity(intent);
            }
        });


        // Show a snackbar with the hint to import new transactions if there aren't any
        if (listAdapter.getCount() == 0){
            Snackbar snackbar = Snackbar
                    .make(listView, "Nothing here yet!", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("IMPORT", new View.OnClickListener() {

                // start CSV reader activity
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, InsightActivity.class);
                    startActivity(intent);
                }
            });
            snackbar.show();
        }
    }

}
