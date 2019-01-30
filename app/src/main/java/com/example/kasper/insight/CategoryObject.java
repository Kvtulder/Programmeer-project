package com.example.kasper.insight;

import java.io.Serializable;

public class CategoryObject implements Serializable {

    private int id;
    private String name;
    private Icon icon;
    private boolean income;
    private boolean spending;

    // constructor for objects that are not in the db, so don't have an ID yet
    public CategoryObject(String name, Icon icon, boolean income, boolean spending) {
        this.name = name;
        this.icon = icon;
        this.income =income;
        this.spending = spending;
    }
    // contructor for db objects, they've an ID
    public CategoryObject(int id, String name, Icon icon ,boolean income, boolean spending) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.income = income;
        this.spending = spending;
    }

    public String getName() {
        return name;
    }

    public int getDrawableID() {
        return icon.getResource();
    }

    public Icon getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }

    public boolean isIncome() {
        return income;
    }

    public boolean isSpending() {
        return spending;
    }
}
