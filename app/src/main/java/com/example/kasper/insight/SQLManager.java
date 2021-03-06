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
    private static final String TABLENAME_LINKED_TRANSACTIONS = "linked_transactions";

    private SQLManager(Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        // init database
        database = getWritableDatabase();
    }

    // make class a singleton
    public static SQLManager getInstance(Context context){

        if(instance == null)
            instance = new SQLManager(context, DATABASENAME, null, 22);
        return instance;
    }

    public int getLinkedCategoryID(String IBAN, double transactionAmount){
        String[] args = {IBAN, String.format("%d",transactionAmount)};
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLENAME_LINKED_TRANSACTIONS + " WHERE 'iban' = ?", args);

        // iban has unique identifier so we have only 0 or 1 results
        if(cursor.getCount() == 1){
            cursor.moveToFirst();
            double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
            int id = cursor.getInt(cursor.getColumnIndex("category_id"));

            // if there's no amount specified we've a match!
            if (amount == 0 || amount == transactionAmount)
                return id;
        }
       // no match, return minus 1
        return -1;
    }

    public void insertLinkedCategory(String IBAN, double transactionAmount, int categoryID){
        ContentValues data = new ContentValues();
        data.put("iban", IBAN);
        data.put("amount", transactionAmount);
        data.put("category_id", categoryID);

        database.insert(TABLENAME_LINKED_TRANSACTIONS, null, data);
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

        ArrayList<CategoryObject> list = cursorToCategoryList(cursor);
        if (list.size() == 1)
            return list.get(0);
        else
            return null;
    }

    public ArrayList<TransactionObject> getTransactions(){
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLENAME_TRANSACTIONS,null);
        return cursorToTransactionList(cursor);
    }

    public ArrayList<CategoryObject> getCategories() {
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLENAME_CATEGORIES, null);
        return cursorToCategoryList(cursor);
    }

    public ArrayList<TransactionObject> getTransactionsWithoutCategory(){
        Cursor cursor = database
                .rawQuery("SELECT * FROM " + TABLENAME_TRANSACTIONS
                        + " WHERE categoryID IS NULL", null);
        return cursorToTransactionList(cursor);
    }

    public ArrayList<TransactionObject> getTransactionsByPeriod(PeriodObject period){

        String selection = "date >= ? AND date <= ?";
        String start = String.valueOf(period.getStart().getTime());
        String end = String.valueOf(period.getEnd().getTime());
        String[] selectionsArgs = {start, end};

        Cursor cursor = database.query(TABLENAME_TRANSACTIONS,null, selection, selectionsArgs,
                null, null, null);
        return cursorToTransactionList(cursor);
    }

    public CategoryObject getCategoryByName(String name){
        String[] whereArgs = {name};

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLENAME_CATEGORIES
                + " WHERE name = ?", whereArgs);

        ArrayList<CategoryObject> list = cursorToCategoryList(cursor);

        if (list.size() != 1)
            return null;
        else
            return list.get(0);
    }

    public ArrayList<TransactionObject> getTransactionsWithCategoryID(int ID){

        String[] whereArgs = {String.format("%d",ID)};
        Cursor cursor = database
                .rawQuery("SELECT * FROM " + TABLENAME_TRANSACTIONS
                        + " WHERE categoryID IS ?", whereArgs);
        return cursorToTransactionList(cursor);

    }

    public ArrayList<CategoryObject> getIncomeCategories(){
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLENAME_CATEGORIES +
                        " WHERE income = 1", null);
        return cursorToCategoryList(cursor);
    }

    public ArrayList<CategoryObject> getSpendingCategories(){
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLENAME_CATEGORIES +
                " WHERE spending = 1", null);
        return cursorToCategoryList(cursor);
    }

    public void insertTransaction(TransactionObject object){

        ContentValues transactionData = new ContentValues();
        Long millisecs = object.getDate().getTime();

        transactionData.put("name", object.getName());
        // sqlite doesn't work with boolean type, so we use integers instead
        transactionData.put("negative", object.getNegative() ? 1 : 0);
        transactionData.put("date", millisecs);
        transactionData.put("amount", object.getAmount());
        transactionData.put("description",object.getDescription());
        transactionData.put("IBAN",object.getIBAN());

        database.insert(TABLENAME_TRANSACTIONS, null, transactionData);
    }

    public void insertCategory(CategoryObject object){

        ContentValues categoryData = new ContentValues();
        categoryData.put("name", object.getName());
        categoryData.put("drawable", object.getIcon().toString());
        int income = object.isIncome() ? 0 : 1;
        int spending = object.isSpending() ? 0 : 1;
        categoryData.put("income", income);
        categoryData.put("spending", spending);

        database.insert(TABLENAME_CATEGORIES, null, categoryData);
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

                TransactionObject transaction = new TransactionObject(id, date, IBAN, name, description,
                        amount, negative, category);
                list.add(transaction);
            }
            while (cursor.moveToNext());
            cursor.close(); // remove from memory
        }
        return list;
    }

    public ArrayList<CategoryObject> cursorToCategoryList(Cursor cursor){

        ArrayList<CategoryObject> list = new ArrayList<>();
        if(cursor.moveToFirst()) {

            // loop as long as there still are rows left
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String iconName = cursor.getString(cursor.getColumnIndex("drawable"));

                // set default icon
                Icon icon = Icon.TRANSAPARENT;

                // change icon from default if any
                if(iconName != null) {
                    icon = Icon.valueOf(iconName);
                }

                int id = cursor.getInt(cursor.getColumnIndex("_id"));

                boolean income = cursor.getInt(cursor.getColumnIndex("income")) == 1;
                boolean spending = cursor.getInt(cursor.getColumnIndex("spending")) == 1;

                CategoryObject category = new CategoryObject(id, name, icon, income, spending);
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
                "income INT," +
                "spending INT," +
                "drawable TEXT);";

        db.execSQL(query);

        // create linked transations table
        query = "CREATE TABLE " + TABLENAME_LINKED_TRANSACTIONS + "(" +
                "iban TEXT PRIMARY KEY," +
                "amount DOUBLE," +
                "category_id INT DEFAULT 0);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // delete database and create again
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME_LINKED_TRANSACTIONS);

        onCreate(db);

    }
}
