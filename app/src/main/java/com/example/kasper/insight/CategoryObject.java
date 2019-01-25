package com.example.kasper.insight;

import java.io.Serializable;

public class CategoryObject implements Serializable {

    private int id;
    private String name;
    private Icon icon;
    private int[] linkedTransactionAccountIDS;
    private boolean income;

    private boolean spending;


    // most simple constructor
    public CategoryObject(String name, Icon icon, boolean income, boolean spending) {
        this.name = name;
        this.icon = icon;
        this.income =income;
        this.spending = spending;
    }

    public CategoryObject(int id, String name, Icon icon ,boolean income, boolean spending) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }


    //TODO imlpement constuctor
    // override constructor
    public CategoryObject(String name, Icon icon, int[] linkedTransactionAccountIDS) {
        this.name = name;
        this.icon = icon;
        this.linkedTransactionAccountIDS = linkedTransactionAccountIDS;
    }

    public String getName() {
        return name;
    }

    public int getDrawableID() {
        return icon.getResource();
    }

    public int[] getLinkedTransactionAccountIDS() {
        return linkedTransactionAccountIDS;
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
