package com.example.kasper.insight;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLManager {

    private static String TABLENAME_TRANSACTIONS = "transactions";
    private static String TABLENAME_CATEGORIES = "categories";



    private class Transactions extends SQLiteOpenHelper{

        

        public Transactions(@Nullable Context context, @Nullable String name,
                            @Nullable SQLiteDatabase.CursorFactory factory, int version) {

            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String query = "CREATE TABLE " + TABLENAME_TRANSACTIONS + "(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "description TEXT," +
                    "amount DOUBLE NOT NULL," +
                    "IBAN TEXT NOT NULL," +
                    "categoryID INT );";

            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public TransactionObject[] getTransactions(){

        // Create some  fake transactions for testing purposes

        TransactionObject object1 = new TransactionObject("NL03 IGNB 0003 2577 45",
                "HEMA", "Volgnummer 0004344 Pasnummer 34545", -7.48, getCategories()[2]);
        TransactionObject object2 = new TransactionObject("NL03 IGNB 0003 1234 45",
                "GAMMA", "Volgnummer 0004344 Pasnummer 34545", -4.38, getCategories()[1]);
        TransactionObject object3 = new TransactionObject("NL03 IGNB 0003 2342 45",
                "Lidl", "Volgnummer 0004344 Pasnummer 34545", -23.20);

        TransactionObject[] array = {object1, object2, object3};

        return array;

    }

    public CategoryObject[] getCategories(){

        // Create some fake categories for testing purposes

        CategoryObject object1 = new CategoryObject("Boodschappen",R.drawable.cutlery,null);
        CategoryObject object2 = new CategoryObject("Vervoer",R.drawable.car,null);
        CategoryObject object3 = new CategoryObject("Huur",R.drawable.home,null);

        CategoryObject[] array = {object1, object2, object3};

        return array;
    }
}
