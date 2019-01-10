package com.example.kasper.insight;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProcressTransactionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procress_transactions);

        ViewPager viewPager = findViewById(R.id.pager);
        TransactionFragmentStatePagerAdapter adapter =
                new TransactionFragmentStatePagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);

    }
}
