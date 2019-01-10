package com.example.kasper.insight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TransactionFragmentStatePagerAdapter  extends FragmentStatePagerAdapter {

    TransactionObject[] transactions;
    SQLManager sqlManager;
    Context context;


    public TransactionFragmentStatePagerAdapter(FragmentManager fm) {

        super(fm);
        sqlManager = new SQLManager();
        transactions = sqlManager.getTransactions();


    }

    @Override
    public int getCount() {
        return transactions.length;
    }

    @Override
    public Fragment getItem(int position) {

        TransactionListFragment fragment = new TransactionListFragment();

        Bundle args = new Bundle();
        args.putSerializable("transaction",transactions[position]);

        // the android api discourages passing data through constructors in fragmens
        // so we pass the Transaction object trough a bundle
        fragment.setArguments(args);

        return fragment;
    }

    public  static class TransactionListFragment extends Fragment{

        private TransactionObject transaction;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle arguments = getArguments();

            // check if there are anu arguments
            if(arguments != null)
            {
                transaction =  (TransactionObject) arguments.getSerializable("transaction");
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


            View view = inflater.inflate(R.layout.transaction_fragment_state, container, false);

            TextView name = view.findViewById(R.id.transactionName);
            TextView description = view.findViewById(R.id.transactionDescription);
            TextView amount = view.findViewById(R.id.transactionAmount);

            name.setText(transaction.getName());
            description.setText(transaction.getDescription());
            amount.setText(String.format("%.2f,-", transaction.getAmount()));
            return view;
        }
    }

}


