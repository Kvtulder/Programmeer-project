package com.example.kasper.insight;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class TransactionViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_view);

        // get the transaction object passed through the bundle.
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            TransactionObject transaction = (TransactionObject) bundle.getSerializable("transaction");

            TextView name = findViewById(R.id.transactionName);
            TextView description = findViewById(R.id.transactionDescription);
            Spinner spinner = findViewById(R.id.spinner);

            CategoryAdapter adapter = new CategoryAdapter(this, R.layout.category_spinner_item);
            spinner.setAdapter(adapter);

            CategoryObject category = transaction.getCategory();

            // set spinner to current category (if it has one)
            if(category != null)
            {
                int position = adapter.getPosition(transaction.getCategory().getName());
                spinner.setSelection(position);
            }

            //TODO implement on item selected listener


            // fill in all the textviews
            name.setText(transaction.getName());
            description.setText(transaction.getDescription());



        }
    }

}
