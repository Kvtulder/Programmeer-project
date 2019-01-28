package com.example.kasper.insight;

public enum Icon {
    BOOK,
    CALCULATOR,
    CALENDAR,
    CAR,
    CUTLERY,
    GAMEPAD,
    HEART,
    HOME,
    IDEA,
    KEY,
    LAPTOP,
    MEDAL,
    MEGAPHONE,
    MONEY,
    MONITOR,
    MUSIC,
    PAINTBRUSH,
    PHOTOCAMERA,
    PLANETEARTH,
    SMARTPHONE,
    TRASH,
    TROPHY,
    TRANSAPARENT;

    // get the resource of the
    public int getResource(){
        switch (this){
            case BOOK:
                return R.drawable.book;
            case CALCULATOR:
                return R.drawable.calculator;
            case CALENDAR:
                return R.drawable.calendar;
            case CAR:
                return R.drawable.car;
            case CUTLERY:
                return R.drawable.cutlery;
            case GAMEPAD:
                return R.drawable.gamepad;
            case HEART:
                return R.drawable.heart;
            case HOME:
                return R.drawable.home;
            case IDEA:
                return R.drawable.idea;
            case KEY:
                return R.drawable.key;
            case LAPTOP:
                return R.drawable.laptop;
            case MEDAL:
                return R.drawable.medal;
            case MEGAPHONE:
                return R.drawable.megaphone;
            case MONEY:
                return R.drawable.money;
            case MONITOR:
                return R.drawable.monitor;
            case MUSIC:
                return R.drawable.music;
            case PAINTBRUSH:
                return R.drawable.paintbrush;
            case PHOTOCAMERA:
                return R.drawable.photocamera;
            case PLANETEARTH:
                return R.drawable.planetearth;
            case SMARTPHONE:
                return R.drawable.smartphone;
            case TRASH:
                return R.drawable.trash;
            case TROPHY:
                return R.drawable.trophy;
            case TRANSAPARENT:
                return android.R.color.transparent;

            // no value found, return a blank resource
            default:
                return android.R.color.transparent;
        }
    }
}
