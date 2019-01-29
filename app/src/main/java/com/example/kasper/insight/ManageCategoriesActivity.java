package com.example.kasper.insight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class ManageCategoriesActivity extends AppCompatActivity {

    private SQLManager sqlManager;
    private CategoryAdapter adapter;
    private GridView gridView;
    int newCategoryItemPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        gridView = findViewById(R.id.gridview);

        sqlManager = SQLManager.getInstance(this);

        ArrayList<CategoryObject> categories = sqlManager.getCategories();
        adapter = new CategoryAdapter(this, R.layout.category_grid_item, categories);
        renew();

        // let the user view categories by clicking on them
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;

                // check if the user clicked on new category item
                if(position == newCategoryItemPosition){
                    intent = new Intent(ManageCategoriesActivity.this, NewCategoryActivity.class);
                }
                else
                {
                    CategoryObject category = (CategoryObject) adapter.getItem(position);
                    intent = new Intent(ManageCategoriesActivity.this, CategoryViewActivity.class);
                    intent.putExtra("category", category);
                }

                startActivity(intent);
                // start it!
            }
        });

    }

    public void renew(){
        ArrayList<CategoryObject> categories = sqlManager.getCategories();
        adapter = new CategoryAdapter(this, R.layout.category_grid_item, categories);
        gridView.setAdapter(adapter);

        final CategoryObject newCategoryItem = new CategoryObject("Nieuwe categorie",
                Icon.BOOK, false, false);
        adapter.addPosition(newCategoryItem);

        // we've added the new item at the end of the grid, so the id equals the total amount - 1
        newCategoryItemPosition = adapter.getCount() - 1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // there might have been created a new category! Make sure everything is up to date
        renew();
    }
}
