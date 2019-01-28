package com.example.kasper.insight;

public interface CSVReaderCallback {

    abstract void onInvalidFile();
    abstract void onParseException();
}
