package com.example.kasper.insight;

import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticsHelper {


    public double getTotal(ArrayList<TransactionObject> transactions){
        double total = 0;

        // loop through all transactions
        for(TransactionObject transaction : transactions){
            total += transaction.getAmount();
        }

        return total;
    }

    // returns the data for the piechart: totals every transaction & returns the negative categories
    public PieDataSet getSpendingPieData(ArrayList<TransactionObject> transactions, String label){
        HashMap<String, Double> categoryTotals = new HashMap<>();
        ArrayList<PieEntry> entries = new ArrayList();


        for(TransactionObject transaction : transactions){
            CategoryObject category = transaction.getCategory();

            double amount = transaction.getAmount();

            // we are looking for withdrawn money so we treat income as negative instead positive
            if(!transaction.isNegative())
                amount *= -1;

            // check if transaction has a category
            if(category != null) {

                // check if we already initialised the category in the totals hashmap
                if (categoryTotals.containsKey(category)) {
                    double previous = categoryTotals.get(category);
                    categoryTotals.put(category.getName(), previous + amount);
                }
                else {
                    // no value yet for this category, create one
                    categoryTotals.put(category.getName(), amount);
                }
            }
        }

        for(Map.Entry<String, Double> entry : categoryTotals.entrySet()){

            // we are only looking for withdrawn money, so remove every negative amount
            // remember we chose to treat income as negative
            if(Double.valueOf(entry.getValue()) > 0)
                entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        return new PieDataSet(entries, label);

    }
}
