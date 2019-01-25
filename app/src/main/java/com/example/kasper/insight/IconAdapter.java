package com.example.kasper.insight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class IconAdapter extends BaseAdapter {

    Icon[] values;
    Context context;

    public IconAdapter(Context context) {
        // load all the images
        values = Icon.values();
        this.context = context;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return values[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.icon_spinner_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);
        Icon icon = values[position];
        imageView.setImageResource(icon.getResource());

        return convertView;
    }
}
