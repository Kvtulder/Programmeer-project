package com.example.kasper.insight;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ProcressTransactionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procress_transactions);

        final Context context = this;

        ViewPager viewPager = findViewById(R.id.pager);
        TransactionFragmentStatePagerAdapter adapter =
                new TransactionFragmentStatePagerAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(adapter);




        // Show a snackbar with the hint to import new transactions if there aren't any
        if (adapter.getCount() == 0){
            Snackbar snackbar = Snackbar
                    .make(viewPager, "Nothing here yet!", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("IMPORT", new View.OnClickListener() {

                // start CSV reader activity
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CSVReaderActivity.class);
                    startActivity(intent);
                }
            });
            snackbar.show();
        }

    }
}
