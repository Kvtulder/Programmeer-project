package com.example.kasper.insight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class ManageCategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        final CategoryAdapter adapter = new CategoryAdapter(this, R.layout.category_grid_item);
        final Context context = this;


        GridView gridView = findViewById(R.id.gridview);
        gridView.setAdapter(adapter);

        // let the user view categories by clicking on them
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CategoryObject category = (CategoryObject) adapter.getItem(position);
                Intent intent = new Intent(context, CategoryViewActivity.class);
                intent.putExtra("category", category);
                // start it!
                startActivity(intent);

            }
        });

    }

}
