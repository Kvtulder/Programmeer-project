package com.example.kasper.insight;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PeriodObject {

    String format;
    Date start;
    Date end;

    // TODO implement custom period selector
    public PeriodObject(Date start, Date end) {

        // set format string
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy");
        simpleDateFormat.format(start.getTime());

        this.format = format;
        this.start = start;
        this.end = end;
    }

    // use default period, one month!
    public PeriodObject(Long startMilliSecs) {


        Date start = new Date(startMilliSecs);
        this.start = start;

        // set format string
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy");
        this.format = simpleDateFormat.format(start.getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.MONTH, 1);

        this.end = calendar.getTime();


        }


    public String getString() {
        return format;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
}
