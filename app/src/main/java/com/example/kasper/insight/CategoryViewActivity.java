package com.example.kasper.insight;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);
        Bundle arguments = getIntent().getExtras();

        // get transaction from bundle
        if(arguments != null)
        {
            CategoryObject category =  (CategoryObject) arguments.getSerializable("category");

            TextView name = findViewById(R.id.nameTextView);
            TextView spendings = findViewById(R.id.spendingTextView);
            ImageView logo = findViewById(R.id.ImageView);

            Drawable drawable = getResources().getDrawable(category.getDrawableID());
            name.setText(category.getName());
            logo.setImageDrawable(drawable);


        }
    }
}
