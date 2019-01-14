package com.example.kasper.insight;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryGridAdapter extends BaseAdapter {

    private SQLManager sqlManager;
    private CategoryObject[] categories;

    Context context;

    public CategoryGridAdapter(Context context) {

        this.context = context;

        sqlManager = SQLManager.getInstance(context);
        categories = sqlManager.getCategories();
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public Object getItem(int position) {
        return categories[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CategoryObject category = categories[position];

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.category_grid_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.categoryLogo);
        Drawable drawable = context.getResources().getDrawable(category.getDrawableID());
        imageView.setImageDrawable(drawable);

        TextView name = convertView.findViewById(R.id.categoryName);
        name.setText(category.getName());

        return convertView;
    }
}
