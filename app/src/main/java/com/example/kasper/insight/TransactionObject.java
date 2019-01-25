package com.example.kasper.insight;

import android.icu.util.ULocale;

import java.io.Serializable;
import java.util.Date;


// make serializable so we can pass the object through a bundle
public class TransactionObject implements Serializable {

    private int ID;
    private Date date;
    private String IBAN;
    private String name;
    private String description;
    private double amount;
    private CategoryObject category;
    private boolean negative;


    // create constructor for new transactions that haven't been stored yet in the db
    // they don't have an ID yet
    public TransactionObject(Date date, String IBAN, String name, String description, double amount,
                             Boolean negative) {
        this.negative = negative;
        this.date = date;
        this.IBAN = IBAN;
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    // contructor for objects from the sql db
    public TransactionObject(int ID, Date date, String IBAN, String name, String description, double amount,
                             boolean negative, CategoryObject category) {

        this.date = date;
        this.negative = negative;
        this.ID = ID;
        this.IBAN = IBAN;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public String getIBAN() {
        return IBAN;
    }

    public boolean getNegative() {
        return negative;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public int getID() {
        return ID;
    }

    public Date getDate() {
        return date;
    }

    public CategoryObject getCategory() { return category; }

    public void setCategory(CategoryObject category) {
        this.category = category;
    }

    public boolean isNegative() {
        return negative;
    }
}
