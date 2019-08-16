package com.elitehogrider.model;

import java.math.BigDecimal;

public class Holding {
    Ticker ticker;
    BigDecimal price;
    BigDecimal shares;

    public Holding(Ticker ticker, BigDecimal price, BigDecimal shares) {
        this.ticker = ticker;
        this.price = price;
        this.shares = shares;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getShares() {
        return shares;
    }

    public void setShares(BigDecimal shares) {
        this.shares = shares;
    }
}
