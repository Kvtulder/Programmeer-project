package com.example.kasper.insight;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        final Spinner imageSelector = findViewById(R.id.imageSelector);
        IconAdapter adapter = new IconAdapter(this);
        imageSelector.setAdapter(adapter);

        final SQLManager sqlManager = SQLManager.getInstance(this);
        final TextView nameText = findViewById(R.id.nameText);

        final CheckBox incomeCheckbox = findViewById(R.id.incomecheckBox);
        final CheckBox spendingCheckbox = findViewById(R.id.spendingCheckbox);

        FloatingActionButton fab = findViewById(R.id.floatingButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                Icon icon = (Icon) imageSelector.getSelectedItem();

                boolean income = incomeCheckbox.isChecked();
                boolean spending = spendingCheckbox.isChecked();

                if (!income && !spending){
                    Toast.makeText(NewCategoryActivity.this, "Minstens een checkbox moet angevinkt zijn!",
                            Toast.LENGTH_SHORT).show();

                    // cancel onclick listener
                    return;
                }

                CategoryObject category = new CategoryObject(name, icon, income, spending);
                sqlManager.insertCategory(category);

                finish();


            }
        });
    }
}
