package com.example.kasper.insight;

import android.icu.util.ULocale;

import java.io.Serializable;


// make serializable so we can pass the object through a bundle
public class TransactionObject implements Serializable {

    private String IBAN;
    private String name;
    private String description;
    private double amount;

    private CategoryObject category;

    public TransactionObject(String IBAN, String name, String description, double amount) {
        this.IBAN = IBAN;
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    public TransactionObject(String IBAN, String name, String description, double amount, CategoryObject category) {
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

    public CategoryObject getCategory() { return category; }

    public void setCategory(CategoryObject category) {
        this.category = category;
    }
}
