package com.elitehogrider.model.indicator;

import yahoofinance.histquotes.HistoricalQuote;

import java.util.HashMap;
import java.util.Map;

public class GenericIndicators implements Comparable<GenericIndicators> {

    HistoricalQuote historicalQuote;
    Map<IndicatorType, Object> indicatorMap;

    public GenericIndicators(HistoricalQuote historicalQuote) {
        this.historicalQuote = historicalQuote;
        this.indicatorMap = new HashMap<>();
    }

    public HistoricalQuote getHistoricalQuote() {
        return historicalQuote;
    }

    public Map<IndicatorType, Object> getIndicatorMap() {
        return indicatorMap;
    }

    @Override
    public int compareTo(GenericIndicators o) {
        return this.historicalQuote.getDate().compareTo(o.historicalQuote.getDate());
    }
}
