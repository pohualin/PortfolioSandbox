package com.elitehogrider.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Trader {

    Long id;
    String name;
    Portfolio portfolio;

    private Trader() {
    }

    public Trader(String name, BigDecimal amount) {
        this.id = UUID.randomUUID().getMostSignificantBits();
        this.name = name;
        this.portfolio = Portfolio.newPortfolio(amount);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }
}
