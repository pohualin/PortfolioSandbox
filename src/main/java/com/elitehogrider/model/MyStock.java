package com.elitehogrider.model;

import yahoofinance.Stock;

public class MyStock {
    Ticker ticker;
    Stock raw;

    public MyStock(Ticker ticker, Stock raw) {
        this.ticker = ticker;
        this.raw = raw;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public Stock getRaw() {
        return raw;
    }

    public void setRaw(Stock raw) {
        this.raw = raw;
    }
}
