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

public class CategoryAdapter extends BaseAdapter {

    private SQLManager sqlManager;
    private ArrayList<CategoryObject> categories;
    private int layoutFile;

    Context context;

    public CategoryAdapter(Context context, int layoutFile) {

        this.context = context;
        this.layoutFile = layoutFile;

        sqlManager = SQLManager.getInstance(context);
        categories = sqlManager.getCategories();
    }

    public void addPosition(CategoryObject object, int position){
        categories.add(position, object);
    }

    public int getPosition(CategoryObject object){
        return categories.indexOf(object);
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CategoryObject category = categories.get(position);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutFile, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.categoryLogo);
        Drawable drawable = context.getResources().getDrawable(category.getDrawableID());
        imageView.setImageDrawable(drawable);

        TextView name = convertView.findViewById(R.id.categoryName);
        name.setText(category.getName());

        return convertView;
    }
}
