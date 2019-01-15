package com.example.kasper.insight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

public class CSVReaderActivity extends AppCompatActivity {


    // create an ID for the anroid file dialog
    int CSVREADREQUESTCODE = 1;

    // store reference to var so we can also can Activity functions from inner classes
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csvreader);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent().setType("text/csv")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Selecteer een CSV bestand"), CSVREADREQUESTCODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            FileDescriptor fd = getContentResolver()
                    .openFileDescriptor(data.getData(), "r").getFileDescriptor();
            CSVReader csvReader = new CSVReader(fd, this);
            csvReader.storeTransactions();

            activity.finish();


        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Can't find selected file!", Toast.LENGTH_LONG).show();
        }

    }
}
