package com.example.kasper.insight;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TransactionListAdapter extends BaseAdapter {

    private TransactionObject[] transactions;
    private Context context;

    public TransactionListAdapter(Context context) {

        // set the context variable
        this.context = context;

        // get the transactions from the local database
        SQLManager sqlManager = new SQLManager();
        transactions = sqlManager.getTransactions();

    }

    @Override
    public int getCount() {

        return transactions.length;
    }

    @Override
    public Object getItem(int position) {

        //TODO Does android handle integers greater than getCount?

        return transactions[position];
    }

    @Override
    public long getItemId(int position) {
        // No special index, just use the position integer
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.transaction_list_item, parent, false);
        }

        TransactionObject transaction = transactions[position];
        TextView name =  convertView.findViewById(R.id.transactionName);
        name.setText(transaction.getName());

        TextView description = convertView.findViewById(R.id.transactionDescription);
        description.setText(transaction.getDescription());

        TextView amount = convertView.findViewById(R.id.transactionAmount);
        amount.setText(String.format("%.2f", transaction.getAmount()));


        CategoryObject category = transaction.getCategory();
        if (category != null){
            ImageView imageView = convertView.findViewById(R.id.imageView);
            Drawable drawable = convertView.getResources().getDrawable(category.getDrawableID());
            imageView.setImageDrawable(drawable);
        }


        return convertView;
    }
}