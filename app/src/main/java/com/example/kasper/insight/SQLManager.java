package com.example.kasper.insight;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLManager extends SQLiteOpenHelper {


    private static SQLManager instance;


    private static String DATABASENAME = "InsightDB";
    private static String TABLENAME_TRANSACTIONS = "transactions";
    private static String TABLENAME_CATEGORIES = "categories";

    private SQLManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    // make class a singleton
    public static SQLManager getInstance(Context context){
        if(instance == null){
            instance = new SQLManager(context, DATABASENAME, null, 5);
        }

        return instance;
    }

    public TransactionObject[] getTransactions(){



        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLENAME_TRANSACTIONS,null);

        // TODO Fix this horrible code
        TransactionObject[] array = new TransactionObject[1];

        if(cursor.moveToFirst()){

            // loop as long as there still are rows left
            do{
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String description = cursor
                        .getString(cursor.getColumnIndex("description"));
                String IBAN = cursor.getString(cursor.getColumnIndex("IBAN"));
                double amount = cursor.getDouble(cursor.getColumnIndex("amount"));

                TransactionObject transaction = new TransactionObject(IBAN, name, description, amount);

                array[0] = transaction;
                return array;

            }
            while (cursor.moveToNext());

            // remove from memory
            //cursor.close();
        }

        // Create some  fake transactions for testing purposes

        TransactionObject object1 = new TransactionObject("NL03 IGNB 0003 2577 45",
                "HEMA", "Volgnummer 0004344 Pasnummer 34545", -7.48, getCategories()[2]);
        TransactionObject object2 = new TransactionObject("NL03 IGNB 0003 1234 45",
                "GAMMA", "Volgnummer 0004344 Pasnummer 34545", -4.38, getCategories()[1]);
        TransactionObject object3 = new TransactionObject("NL03 IGNB 0003 2342 45",
                "Lidl", "Volgnummer 0004344 Pasnummer 34545", -23.20);



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

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create transactions table
        String query = "CREATE TABLE " + TABLENAME_TRANSACTIONS + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "amount DOUBLE NOT NULL," +
                "IBAN TEXT NOT NULL," +
                "categoryID INT );";

        db.execSQL(query);

        // create categories table
        query = "CREATE TABLE " + TABLENAME_CATEGORIES + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "drawable INT);";

        db.execSQL(query);

        // insert test value
        String test = "INSERT INTO " + TABLENAME_CATEGORIES + " (name, drawable) VALUES " +
                "('Boodschappen', " + R.drawable.cutlery + ");";

        db.execSQL(test);


        // insert test value
        test = "INSERT INTO " + TABLENAME_TRANSACTIONS + " (name, description, amount, IBAN) VALUES " +
                "('HEMA!', 'Volgnummer 0004344 Pasnummer 34545', -7.48, 'NL03 IGNB 0003 2577 45');";

        db.execSQL(test);

        test = "INSERT INTO " + TABLENAME_TRANSACTIONS + " (name, description, amount, IBAN) VALUES " +
                "('GAMMA!', 'Volgnummer 0004344 Pasnummer 34545', -3.48, 'NL03 IGNB 0003 2577 45');";

        db.execSQL(test);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //TODO Only for testing purposes: this deletes all user data!

        // delete database and create again
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME_CATEGORIES);
        onCreate(db);

    }
}
