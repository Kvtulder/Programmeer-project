package com.example.kasper.insight;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TransactionListAdapter extends BaseAdapter {

    private ArrayList<TransactionObject> transactions;
    private Context context;

    public TransactionListAdapter(Context context, ArrayList<TransactionObject> transactions) {

        // set the context variable
        this.context = context;
        this.transactions = transactions;

    }

    @Override
    public int getCount() {

        return transactions.size();
    }

    @Override
    public Object getItem(int position) {

        //TODO Does android handle integers greater than getCount?

        return transactions.get(position);
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

        TransactionObject transaction = transactions.get(position);
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
        else{
            ImageView imageView = convertView.findViewById(R.id.imageView);
            imageView.setImageResource(android.R.color.transparent);
        }


        return convertView;
    }
}
