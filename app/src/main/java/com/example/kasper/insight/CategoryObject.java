package com.example.kasper.insight;

import java.io.Serializable;

public class CategoryObject implements Serializable {

    private int id;
    private String name;
    private int drawable;
    private int[] linkedTransactionAccountIDS;


    // most simple constructor
    public CategoryObject(String name, int drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    public CategoryObject(int id, String name, int drawable) {
        this.id = id;
        this.name = name;
        this.drawable = drawable;
    }


    // override constructor
    public CategoryObject(String name, int drawable, int[] linkedTransactionAccountIDS) {
        this.name = name;
        this.drawable = drawable;
        this.linkedTransactionAccountIDS = linkedTransactionAccountIDS;
    }

    public String getName() {
        return name;
    }

    public int getDrawableID() {
        return drawable;
    }

    public int[] getLinkedTransactionAccountIDS() {
        return linkedTransactionAccountIDS;
    }
}
