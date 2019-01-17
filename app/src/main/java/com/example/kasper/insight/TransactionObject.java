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

    public TransactionObject(Date date, String IBAN, String name, String description, double amount) {
        this.date = date;
        this.IBAN = IBAN;
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    public TransactionObject(int ID, String IBAN, String name, String description, double amount) {
        this.ID = ID;
        this.IBAN = IBAN;
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    public TransactionObject(int ID, String IBAN, String name, String description, double amount, CategoryObject category) {
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
}
