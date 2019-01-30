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

    private ArrayList<CategoryObject> categories;
    private int layoutFile;
    private Context context;

    public CategoryAdapter(Context context, int layoutFile, ArrayList<CategoryObject> categories) {
        this.context = context;
        this.layoutFile = layoutFile;
        this.categories = categories;
    }

    public void addPosition(CategoryObject object, int position){
        categories.add(position, object);
    }

    // overide method for when the object can be inserted at the end (like a queue)
    public void addPosition(CategoryObject object){
        categories.add(object);
    }

    public int getPosition(String categoryName){

        for(CategoryObject category : categories){
            if (category.getName().equals(categoryName))
                return categories.indexOf(category);
        }
        // nothing found yet: return minus 1
        return -1;
    }

    public int getPositionByID(int id){
        for(CategoryObject category : categories){
            if (category.getId() == id)
                return categories.indexOf(category);
        }
        // nothing found yet: return -1
        return -1;
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

        // inflate layout file
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutFile, parent, false);
        }

        // set the icon
        ImageView imageView = convertView.findViewById(R.id.categoryLogo);
        Drawable drawable = context.getResources().getDrawable(category.getDrawableID());
        imageView.setImageDrawable(drawable);

        TextView name = convertView.findViewById(R.id.categoryName);
        name.setText(category.getName());

        return convertView;
    }
}