package com.example.kasper.insight;

import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticsHelper {

    ArrayList<TransactionObject> transactions;
    private boolean countNegative;
    private boolean countPositive;
    private HashMap<String, Double> categoryTotals = new HashMap<>();
    private ArrayList<PieEntry> entries = new ArrayList();


    public StatisticsHelper(ArrayList<TransactionObject> transactions, boolean countNegative, boolean countPositive) {
        this.transactions = transactions;
        this.countPositive = countPositive;
        this.countNegative = countNegative;

        for(TransactionObject transaction : transactions){
            CategoryObject category = transaction.getCategory();

            double amount = transaction.getAmount();

            // we are looking for withdrawn money so we treat income as negative instead positive
            // or we are looking for income, so we treat withdrawn as positive instead negative
            if((!transaction.isNegative() && countNegative) || (transaction.isNegative() && countPositive))
                amount *= -1;

            // check if transaction has a category
            if(category != null) {

                // check if we already initialised the category in the totals hashmap
                if (categoryTotals.containsKey(category.getName())) {
                    double previous = categoryTotals.get(category.getName());
                    categoryTotals.put(category.getName(), previous + amount);
                }
                else {
                    // no value yet for this category, create one
                    categoryTotals.put(category.getName(), amount);
                }
            }
        }
    }


    public double getTotal(){
        double total = 0;
        // loop through all transactions
        for(TransactionObject transaction : transactions){

            double amount = transaction.getAmount();

            // check if transaction is negative and get the amount
            if(transaction.isNegative() && countNegative)
                total += amount * -1;
            else if(!transaction.isNegative() && countPositive)
                total += amount;
        }
        return total;
    }

    public PieDataSet getPieData(String label){

        for(Map.Entry<String, Double> entry : categoryTotals.entrySet()){
            // we are only looking for withdrawn money, so remove every negative amount
            // remember we chose to treat income as negative
            if(Double.valueOf(entry.getValue()) > 0)
                entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        return new PieDataSet(entries, label);
    }
}
