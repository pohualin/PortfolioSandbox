package com.elitehogrider.model;

import yahoofinance.histquotes.HistoricalQuote;

public abstract class Indicators implements Comparable<Indicators> {

    HistoricalQuote historicalQuote;

    public Indicators(HistoricalQuote historicalQuote) {
        this.historicalQuote = historicalQuote;
    }

    public HistoricalQuote getHistoricalQuote() {
        return historicalQuote;
    }

    @Override
    public int compareTo(Indicators o) {
        return this.historicalQuote.getDate().compareTo(o.historicalQuote.getDate());
    }
}
