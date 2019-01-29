package com.example.kasper.insight;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;

public class DateSpinnerAdapter extends BaseAdapter {

    ArrayList<PeriodObject> dateItems = new ArrayList<>();
    Context context;

    public DateSpinnerAdapter(Context context) {

        this.context = context;

        // get first imported transaction
        SQLManager sqlManager = SQLManager.getInstance(context);
        Long millisecs = sqlManager.getOldestTransaction();

        // create two calendars, current date and oldest date
        Calendar calendarIterator = Calendar.getInstance();
        Calendar firstTransaction = Calendar.getInstance();

        // if there is no transaction, set oldest to current datetime
        if(millisecs != null)
            firstTransaction.setTimeInMillis(millisecs);

        // calculate difference
        int years = calendarIterator.get(Calendar.YEAR) - firstTransaction.get(Calendar.YEAR);
        int months = calendarIterator.get(Calendar.MONTH) - firstTransaction.get(Calendar.MONTH);
        months++; // current month is also an option: add 1

        // calculate the total spinner items, one for every month
        int total = years * 12 + months;

        // match months with calendar months
        calendarIterator.set(Calendar.DAY_OF_MONTH,1);

        // create period objects for every spinner item
        for(int i = 0; i < total; i++){

            // start with the last month, than go back until no months left
            PeriodObject period = new PeriodObject(calendarIterator.getTimeInMillis());
            calendarIterator.add(Calendar.MONTH, -1);
            dateItems.add(period);
        }
    }

    @Override
    public int getCount() {
        return dateItems.size();
    }

    @Override
    public Object getItem(int position) {
        return dateItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // inflate the resource file
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.date_spinner_item, parent, false);
        }

        // set the textview
        TextView nameText = convertView.findViewById(R.id.nameText);
        PeriodObject dateItem = dateItems.get(position);
        nameText.setText(dateItem.getString());

        return convertView;

    }
}
