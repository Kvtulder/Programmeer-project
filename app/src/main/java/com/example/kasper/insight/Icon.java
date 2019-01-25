package com.example.kasper.insight;

public enum Icon {
    BOOK,
    CAR,
    HEART,
    HOME,
    LAPTOP,
    TRANSAPARENT;

    // get the resource of the
    public int getResource(){
        switch (this){
            case BOOK:
                return R.drawable.book;
            case CAR:
                return R.drawable.car;
            case HEART:
                return R.drawable.heart;
            case HOME:
                return R.drawable.home;
            case LAPTOP:
                return R.drawable.laptop;
            case TRANSAPARENT:
                return android.R.color.transparent;

            // no value found, return a blank resource
            default:
                return android.R.color.transparent;
        }
    }
}
