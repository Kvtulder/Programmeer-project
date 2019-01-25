package com.example.kasper.insight;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TransactionViewActivity extends AppCompatActivity {

    SQLManager sqlManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_view);

        // get the transaction object passed through the bundle.
        Bundle bundle = getIntent().getExtras();

        sqlManager = SQLManager.getInstance(this);

        if(bundle != null){
            final TransactionObject transaction =
                    (TransactionObject) bundle.getSerializable("transaction");

            TextView name = findViewById(R.id.transactionName);
            TextView description = findViewById(R.id.transactionDescription);
            TextView date = findViewById(R.id.transactionDate);

            final Spinner spinner = findViewById(R.id.spinner);

            ArrayList<CategoryObject> categories;

            if(transaction.isNegative())
                categories = sqlManager.getIncomeCategories();
            else
                categories = sqlManager.getSpendingCategories();

            CategoryAdapter adapter = new CategoryAdapter(this,
                    R.layout.category_spinner_item, categories);
            spinner.setAdapter(adapter);

            CategoryObject category = transaction.getCategory();

            // set spinner to current category (if it has one)
            if(category != null)
            {
                int position = adapter.getPosition(transaction.getCategory().getName());
                spinner.setSelection(position);
            }

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    CategoryObject category = (CategoryObject) spinner.getItemAtPosition(position);
                    sqlManager.setTransactionCategory(transaction, category.getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //TODO implement on item selected listener


            // fill in all the textviews
            name.setText(transaction.getName());
            description.setText(transaction.getDescription());
            String dateString = new SimpleDateFormat("dd-MM-yyyy").format(transaction.getDate());
            date.setText(dateString);



        }
    }

}
