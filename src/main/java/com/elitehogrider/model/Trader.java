package com.elitehogrider.model;


import java.math.BigDecimal;

public class Trader {

    private Trader() {
    }

    public static Trader newTrader(String name, BigDecimal amount) {
        Trader trader = new Trader();
        trader.name = name;
        trader.portfolio = Portfolio.newPortfolio(amount);
        return trader;
    }

    String name;
    Portfolio portfolio;

    public String getName() {
        return name;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }
}
