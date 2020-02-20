package com.elitehogrider.model.param;

import yahoofinance.histquotes.Interval;

import java.util.Calendar;

public class MovingAverageParam extends Param {

    public MovingAverageParam(String ticker, int period) {
        super(ticker);
        this.period = period;
    }

    public MovingAverageParam(String ticker, int period, Interval interval, Calendar from, Calendar to) {
        super(ticker, interval, from, to);
        this.period = period;
    }

    int period;

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
