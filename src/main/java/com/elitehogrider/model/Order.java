package com.elitehogrider.model;

import java.math.BigDecimal;
import java.util.Calendar;

public class Order {
    Calendar date;
    Ticker ticker;
    TradeType type;
    BigDecimal price;
    BigDecimal shares;

    public Order(Calendar date, Ticker ticker, TradeType type, BigDecimal price, BigDecimal shares) {
        this.date = date;
        this.ticker = ticker;
        this.type = type;
        this.price = price;
        this.shares = shares;
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

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getShares() {
        return shares;
    }

}
