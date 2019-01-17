package com.example.kasper.insight;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TransactionFragmentStatePagerAdapter  extends FragmentStatePagerAdapter {

    ArrayList<TransactionObject> transactions;
    SQLManager sqlManager;
    Context context;


    public TransactionFragmentStatePagerAdapter(Context context, FragmentManager fm) {

        super(fm);
        sqlManager = SQLManager.getInstance(context);
        transactions = sqlManager.getTransactions();


    }

    @Override
    public int getCount() {
        return transactions.size();
    }

    @Override
    public Fragment getItem(int position) {

        TransactionListFragment fragment = new TransactionListFragment();

        Bundle args = new Bundle();
        args.putSerializable("transaction",transactions.get(position));

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

            Spinner spinner = view.findViewById(R.id.spinner);
            final CategoryAdapter adapter = new CategoryAdapter(getContext(), R.layout.category_spinner_item);


            // creare a extra option in the spinner that functions as no category
            CategoryObject defaultItem = new CategoryObject("Selecteer een categorie", android.R.color.transparent);
            adapter.addPosition(defaultItem, 0);

            // check if there is already a default category
            if (transaction.getCategory() != null){
                int position = adapter.getPosition(transaction.getCategory());
                spinner.setSelection(position);
            }

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position != 0) {
                        SQLManager sqlManager = SQLManager.getInstance(getContext());
                        CategoryObject category = (CategoryObject) adapter.getItem(position);
                        Boolean bool = sqlManager.setTransactionCategory(transaction, category.getId());

                        if(!bool)
                            Toast.makeText(getContext(), "FALSE!", Toast.LENGTH_LONG).show();
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinner.setAdapter(adapter);

            return view;
        }
    }

}


