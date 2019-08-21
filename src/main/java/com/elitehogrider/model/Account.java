package com.elitehogrider.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Account {

    Long id;
    Portfolio portfolio;
    BigDecimal value;
    BigDecimal cash;
    List<Holding> holdings;

    public Account(BigDecimal amount, Portfolio portfolio) {
        this.id = UUID.randomUUID().getMostSignificantBits();
        this.portfolio = portfolio;
        this.value = amount;
        this.cash = amount;
        this.holdings = new ArrayList<>();
    }

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

    public List<Holding> getHoldings() {
        return holdings;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public List<Holding> getHoldingByTicker(Ticker ticker) {
        return holdings.stream()
                .filter(holding -> holding.getTicker().equals(ticker))
                .collect(Collectors.toList());
    }
}
