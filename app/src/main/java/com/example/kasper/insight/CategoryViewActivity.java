package com.example.kasper.insight;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryViewActivity extends AppCompatActivity {

    private TextView name;
    private TextView spendings;
    private ImageView logo;
    private TextView description;
    private TransactionListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);
        Bundle arguments = getIntent().getExtras();

        // get transaction from bundle
        if(arguments != null)
        {
            CategoryObject category =  (CategoryObject) arguments.getSerializable("category");

            // load all the views in the resource file
            name = findViewById(R.id.nameTextView);
            spendings = findViewById(R.id.spendingTextView);
            logo = findViewById(R.id.ImageView);
            description = findViewById(R.id.listDescriptionText);

            // set all the text/icons of the views

            Drawable drawable = getResources().getDrawable(category.getDrawableID());
            name.setText(category.getName());
            logo.setImageDrawable(drawable);
            description.setText(String.format("Alle transactions in %s", category.getName()));

            // get all the transactions in the category in init the listview
            ListView listView = findViewById(R.id.listView);
            SQLManager sqlManager = SQLManager.getInstance(this);
            ArrayList<TransactionObject> transactions =
                    sqlManager.getTransactionsWithCategoryID(category.getId());
            adapter = new TransactionListAdapter(this,transactions);
            listView.setAdapter(adapter);

            // get total of all the transactions
            StatisticsHelper helper = new StatisticsHelper(transactions, true, true);
            double total = helper.getTotal();

            if(total < 0){
                spendings.setTextColor(Color.RED);
                total *= -1; // take absolute value
            }
            // don't change color if amount is zero
            else if (total > 0) {
                spendings.setTextColor(Color.GREEN);
            }

            spendings.setText(String.format("Totaal: %.2f", total));

            // set a click listener for the listview
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // start the transaction view activity
                    Intent intent = new Intent(CategoryViewActivity.this,
                            TransactionViewActivity.class);
                    TransactionObject transaction = (TransactionObject) adapter.getItem(position);
                    intent.putExtra("transaction", transaction);
                    startActivity(intent);
                }
            });
        }
    }
}
