package com.example.kasper.insight;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CSVReader {

    private FileDescriptor fileDescriptor;
    Context context;

    public CSVReader(FileDescriptor fileDescriptor, Context context) {
        this.context = context;
        this.fileDescriptor = fileDescriptor;
    }

    public ArrayList<TransactionObject> storeTransactions(CSVReaderCallback callback) {

        SQLManager sqlManager = SQLManager.getInstance(context);
        ArrayList<TransactionObject> list = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileDescriptor));

            String line;
            line = reader.readLine();
            Boolean headers = true;

            while (line != null) {
                Log.i("Insight", line);

                // skip first line
                if (headers)
                    headers = false;
                else {
                    String[] args = line.split("\",\"");

                    // check if file has enough argmuments
                    if(args.length < 9){
                        callback.onInvalidFile();
                        return list;
                    }

                    // first and last argument start/end with an ". we need to remove this
                    args[0] = args[0].substring(1);
                    int last = args.length - 1;
                    args[last] = args[last].substring(0, args[last].length() - 1);

                    // parse date, name and iban
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    Date date = dateFormat.parse(args[0]);
                    String name = args[1];
                    String IBAN = args[2];

                    // replace comma's by points, otherwise java will not interpret
                    String amountString = args[6].replace(",",".");
                    Log.i("Insight", "AMOUNT " + amountString);
                    Double amount = Double.parseDouble(amountString);

                    boolean negative = args[5].equals("Af");
                    String description = args[8];

                    TransactionObject object = new TransactionObject(date, IBAN, name, description, amount, negative);
                    sqlManager.insertTransaction(object);
                }
                line = reader.readLine();
            }
        // handle errors
        } catch (IOException e) {
            callback.onInvalidFile();
        } catch (ParseException e) {
            callback.onParseException();
        }
        return list;
    }

    // create callback interface to force proper error handling
    public interface CSVReaderCallback {
        void onInvalidFile();
        void onParseException();
    }

}
