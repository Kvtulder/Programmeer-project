package com.example.kasper.insight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class ManageCategoriesActivity extends AppCompatActivity {

    SQLManager sqlManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        sqlManager = SQLManager.getInstance(this);
        ArrayList<CategoryObject> categories = sqlManager.getCategories();

        final CategoryAdapter adapter = new CategoryAdapter(this, R.layout.category_grid_item,
                categories);


        GridView gridView = findViewById(R.id.gridview);
        gridView.setAdapter(adapter);

        final CategoryObject newCategoryItem = new CategoryObject("Nieuwe categorie",
                Icon.BOOK, false, false);
        adapter.addPosition(newCategoryItem);
        final int newCategoryItemPosition = gridView.getLastVisiblePosition();

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

}
