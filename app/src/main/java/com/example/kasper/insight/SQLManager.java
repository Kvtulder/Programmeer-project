package com.example.kasper.insight;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.Nullable;

public class SQLManager extends SQLiteOpenHelper {

    private static SQLManager instance;
    private SQLiteDatabase database;


    private static final String DATABASENAME = "InsightDB";
    private static final String TABLENAME_TRANSACTIONS = "transactions";
    private static final String TABLENAME_CATEGORIES = "categories";

    private SQLManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        // init database
        database = getWritableDatabase();
    }

    // make class a singleton
    public static SQLManager getInstance(Context context){

        if(instance == null)
            instance = new SQLManager(context, DATABASENAME, null, 12);

        return instance;
    }


    // returns the date of the oldest transaction
    public Long getOldestTransaction(){

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLENAME_TRANSACTIONS +
                " ORDER BY date ASC LIMIT 1", null);

        if (cursor.getCount() != 1)
            return null;
        else {
            cursor.moveToFirst();
            return cursor.getLong(cursor.getColumnIndex("date"));
        }

    }

    public Boolean setTransactionCategory(TransactionObject transaction, int categoryID){

        // check if given transaction has an category
        if (transaction.getID() == 0)
            return false;

        // prepare values
        ContentValues values = new ContentValues();
        values.put("categoryID", categoryID);

        Log.d("Insight", "putting category: t id " + transaction.getID() + "; c id " + categoryID);

        String[] whereArgs = {String.format("%d", transaction.getID())};
        int rowsAffected = database.update(TABLENAME_TRANSACTIONS,values,"_id = ?", whereArgs);

        Log.d("Insight", "rows affectes " + rowsAffected);

        // check if sql query was a succes
        return rowsAffected == 1;
    }

    public CategoryObject getCategoryByID(int ID)
    {
        String[] args = {String.format("%d",ID)};
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLENAME_CATEGORIES + " WHERE _id = ?", args);

        Log.d("Insight",  cursor.getCount() + " ; " + ID);



        ArrayList<CategoryObject> list = cursorToCategoryList(cursor);
        if (list.size() == 1){
            return list.get(0);
        }
        else{
            Log.d("Insight", "Warning: getCategory object returned zero results!" + list.size());
            return null;
        }

    }

    public ArrayList<TransactionObject> getTransactions(){

        // perform database query
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLENAME_TRANSACTIONS,null);

        // transform database results to usable data and return the result
        return cursorToTransactionList(cursor);

    }

    public ArrayList<CategoryObject> getCategories(){

        // perform database query
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLENAME_CATEGORIES,null);

        // transform database results to usable data and return the result
        return cursorToCategoryList(cursor);

    }

    public ArrayList<TransactionObject> getTransactionsWithoutCategory(){

        Cursor cursor = database
                .rawQuery("SELECT * FROM " + TABLENAME_TRANSACTIONS
                        + " WHERE categoryID IS NULL", null);

        return cursorToTransactionList(cursor);

    }

    public CategoryObject getCategoryByName(String name){
        String[] whereArgs = {name};

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLENAME_CATEGORIES
                + " WHERE name = ?", whereArgs);

        ArrayList<CategoryObject> list = cursorToCategoryList(cursor);

        if (list.size()!=1)
            return null;
        else
            return list.get(0);
    }


    public ArrayList<TransactionObject> getTransactionsWithtCategoryID(int ID){

        String[] whereArgs = {String.format("%d",ID)};

        Cursor cursor = database
                .rawQuery("SELECT * FROM " + TABLENAME_TRANSACTIONS
                        + " WHERE categoryID IS ?", whereArgs);

        return cursorToTransactionList(cursor);

    }

    public void insertTransaction(TransactionObject object){

        ContentValues transactionData = new ContentValues();

        transactionData.put("name", object.getName());

        Long millisecs = object.getDate().getTime();

        // sqlite doesn't work with boolean type, so we use integers instead

        transactionData.put("negative", object.getNegative() ? 1 : 0);
        transactionData.put("date", millisecs);
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
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String description = cursor
                        .getString(cursor.getColumnIndex("description"));

                String IBAN = cursor.getString(cursor.getColumnIndex("IBAN"));
                double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                int categoryID = cursor.getInt(cursor.getColumnIndex("categoryID"));

                Long millisecs = cursor.getLong(cursor.getColumnIndex("date"));
                Date date = new Date(millisecs);

                boolean negative = cursor.getInt(cursor.getColumnIndex("negative")) == 1;

                CategoryObject category = getCategoryByID(categoryID);

                if(category!=null)
                    Log.d("Insight",category.getName());

                TransactionObject transaction = new TransactionObject(id, date, IBAN, name, description,
                        amount, negative, category);
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
                "date INT NOT NULL," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "amount DOUBLE NOT NULL," +
                "IBAN TEXT NOT NULL," +
                "negative INT NOT NULL," +
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
