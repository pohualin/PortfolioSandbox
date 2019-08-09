package com.elitehogrider.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Portfolio {

    private Portfolio() {
    }

    public static Portfolio newPortfolio(BigDecimal amount) {
        Portfolio portfolio = new Portfolio();
        portfolio.value = amount;
        portfolio.cash = amount;
        portfolio.holdings = new HashMap<>();
        return portfolio;
    }

    BigDecimal value;
    BigDecimal cash;
    Map<Ticker, List<Holding>> holdings;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public Map<Ticker, List<Holding>> getHoldings() {
        return holdings;
    }
}
