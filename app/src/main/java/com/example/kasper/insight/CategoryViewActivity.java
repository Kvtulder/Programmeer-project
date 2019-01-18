package com.example.kasper.insight;

import android.content.Context;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);
        Bundle arguments = getIntent().getExtras();
        final Context context = this;

        // get transaction from bundle
        if(arguments != null)
        {
            CategoryObject category =  (CategoryObject) arguments.getSerializable("category");

            TextView name = findViewById(R.id.nameTextView);
            TextView spendings = findViewById(R.id.spendingTextView);
            ImageView logo = findViewById(R.id.ImageView);

            Drawable drawable = getResources().getDrawable(category.getDrawableID());
            name.setText(category.getName());
            logo.setImageDrawable(drawable);

            ListView listView = findViewById(R.id.listView);
            SQLManager sqlManager = SQLManager.getInstance(this);
            ArrayList<TransactionObject> transactions =
                    sqlManager.getTransactionsWithtCategoryID(category.getId());
            final TransactionListAdapter adapter = new TransactionListAdapter(this,transactions);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(context, TransactionViewActivity.class);

                    TransactionObject transaction = (TransactionObject) adapter.getItem(position);
                    intent.putExtra("transaction", transaction);
                    startActivity(intent);
                }
            });



        }
    }
}
