package com.elitehogrider.model.param;

import yahoofinance.histquotes.Interval;

import java.util.Calendar;

public class DayOfWeekParam extends Param {

    public DayOfWeekParam(String ticker, int dayOfWeek) {
        super(ticker);
        this.dayOfWeek = dayOfWeek;
    }

    public DayOfWeekParam(String ticker, int dayOfWeek, Interval interval, Calendar from, Calendar to) {
        super(ticker, interval, from, to);
        this.dayOfWeek = dayOfWeek;
    }

    int dayOfWeek;

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
