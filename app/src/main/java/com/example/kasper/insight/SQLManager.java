package com.example.kasper.insight;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
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
            instance = new SQLManager(context, DATABASENAME, null, 7);
        }

        return instance;
    }

    public ArrayList<TransactionObject> getTransactions(){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLENAME_TRANSACTIONS,null);

        // transform database results to usable data and return the result
        return cursorToTransactionList(cursor);

    }

    public ArrayList<CategoryObject> getCategories(){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLENAME_CATEGORIES,null);

        return cursorToCategoryList(cursor);

    }

    public void insertTransaction(TransactionObject object){

        SQLiteDatabase database = getWritableDatabase();
        ContentValues transactionData = new ContentValues();

        transactionData.put("name", object.getName());
        transactionData.put("amount", object.getAmount());
        transactionData.put("description",object.getDescription());
        transactionData.put("IBAN",object.getIBAN());
        //TODO put category

        database.insert(TABLENAME_TRANSACTIONS, null, transactionData);

    }

    // Transforms the cursor from the database into an arraylist with transactions
    private ArrayList<TransactionObject> cursorToTransactionList(Cursor cursor){

        ArrayList<TransactionObject> list = new ArrayList<>();
        if(cursor.moveToFirst()){

            // loop as long as there still are rows left
            do{
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String description = cursor
                        .getString(cursor.getColumnIndex("description"));
                String IBAN = cursor.getString(cursor.getColumnIndex("IBAN"));
                double amount = cursor.getDouble(cursor.getColumnIndex("amount"));

                TransactionObject transaction = new TransactionObject(IBAN, name, description, amount);
                list.add(transaction);
            }
            while (cursor.moveToNext());

            // remove from memory
            cursor.close();
        }

        return list;
    }

    public ArrayList<CategoryObject> cursorToCategoryList(Cursor cursor){

        ArrayList<CategoryObject> list = new ArrayList<>();

        if(cursor.moveToFirst()) {

            // loop as long as there still are rows left
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int drawable = cursor.getInt(cursor.getColumnIndex("drawable"));
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                CategoryObject category = new CategoryObject(id, name, drawable);
                list.add(category);
            }
            while (cursor.moveToNext());

            //remove from memory
            cursor.close();
        }

        return list;

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
        test = "INSERT INTO " + TABLENAME_CATEGORIES + " (name, drawable) VALUES " +
                "('Huur', " + R.drawable.home + ");";

        db.execSQL(test);

        // insert test value
        test = "INSERT INTO " + TABLENAME_CATEGORIES + " (name, drawable) VALUES " +
                "('Auto', " + R.drawable.car + ");";

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
