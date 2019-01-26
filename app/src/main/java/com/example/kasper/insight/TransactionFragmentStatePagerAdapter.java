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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
        transactions = sqlManager.getTransactionsWithoutCategory();

        this.context = context;
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

        // the android api discourages passing data through constructors in fragments
        // so we pass the transaction object trough a bundle
        fragment.setArguments(args);
        return fragment;
    }

    public  static class TransactionListFragment extends Fragment{

        private TransactionObject transaction;
        private View view;
        private TextView name;
        private TextView description;
        private TextView amount;
        private RadioButton ibanMatch;
        private RadioButton ibanAmountMatch;
        private RadioButton neverMatch;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle arguments = getArguments();

            // check if there are any arguments
            if(arguments != null)
                transaction =  (TransactionObject) arguments.getSerializable("transaction");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


            view = inflater.inflate(R.layout.transaction_fragment_state, container, false);

            name = view.findViewById(R.id.transactionName);
            description = view.findViewById(R.id.transactionDescription);
            amount = view.findViewById(R.id.transactionAmount);

            ibanAmountMatch = view.findViewById(R.id.amountIbanMatch);
            ibanMatch = view.findViewById(R.id.ibanMatch);
            neverMatch = view.findViewById(R.id.neverMatch);

            name.setText(transaction.getName());
            description.setText(transaction.getDescription());
            amount.setText(String.format("%.2f,-", transaction.getAmount()));

            ArrayList<CategoryObject> categories;

            SQLManager sqlManager = SQLManager.getInstance(getContext());

            if(transaction.isNegative())
                categories = sqlManager.getIncomeCategories();
            else
                categories = sqlManager.getSpendingCategories();

            final Spinner spinner = view.findViewById(R.id.spinner);
            final CategoryAdapter adapter = new CategoryAdapter(getContext(), R.layout.category_spinner_item, categories);


            // creare a extra option in the spinner that functions as no category
            CategoryObject defaultItem = new CategoryObject("Selecteer een categorie",
                    Icon.TRANSAPARENT, false, false);
            adapter.addPosition(defaultItem, 0);

            // check if there is already a default category
            if (transaction.getCategory() != null){
                int position = adapter.getPosition(transaction.getCategory().getName());
                spinner.setSelection(position);
            }

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position != 0) {

                        neverMatch.setEnabled(true);
                        ibanAmountMatch.setEnabled(true);
                        ibanMatch.setEnabled(true);

                        SQLManager sqlManager = SQLManager.getInstance(getContext());

                        CategoryObject category = (CategoryObject) spinner.getItemAtPosition(position);
                        sqlManager.setTransactionCategory(transaction, category.getId());
                    }

                    else{

                        neverMatch.setEnabled(false);
                        ibanAmountMatch.setEnabled(false);
                        ibanMatch.setEnabled(false);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // do nothing `
                }
            });

            spinner.setAdapter(adapter);

            return view;
        }
    }
}