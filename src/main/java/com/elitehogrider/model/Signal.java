package com.elitehogrider.model;

import java.util.Calendar;

public class Signal {
    Calendar date;
    Ticker ticker;
    TradeType type;
    Indicators indicators;

    public Signal(Calendar date, Ticker ticker, TradeType type, Indicators indicators) {
        this.date = date;
        this.ticker = ticker;
        this.type = type;
        this.indicators = indicators;
    }

    public Calendar getDate() {
        return date;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public TradeType getType() {
        return type;
    }

    public Indicators getIndicators() {
        return indicators;
    }
}
