package com.elitehogrider.model.param;

import yahoofinance.histquotes.Interval;

import java.util.Calendar;

public abstract class Param {

    String ticker;
    Interval interval;
    Calendar from;
    Calendar to;

    Param(String ticker) {
        Calendar today = Calendar.getInstance();
        Calendar from = (Calendar) today.clone();
        from.add(Calendar.MONTH, -1);
        this.ticker = ticker;
        this.interval = Interval.DAILY;
        this.from = from;
        this.to = today;
    }

    Param(String ticker, Interval interval, Calendar from, Calendar to) {
        this.ticker = ticker;
        this.interval = interval;
        this.from = from;
        this.to = to;
    }


    public String getTicker() {
        return ticker;
    }

    public Interval getInterval() {
        return interval;
    }

    public Calendar getFrom() {
        return from;
    }

    public Calendar getTo() {
        return to;
    }
}
