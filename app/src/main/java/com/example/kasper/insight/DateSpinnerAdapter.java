package com.example.kasper.insight;

import android.content.Context;
import android.icu.util.LocaleData;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Calendar;
import java.util.Date;

public class DateSpinnerAdapter extends BaseAdapter {

    SQLManager sqlManager;

    public DateSpinnerAdapter(Context context) {

        // get first imported transaction
        SQLManager sqlManager = SQLManager.getInstance(context);

        Long millisecs = sqlManager.getOldestTransaction();
        







    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
