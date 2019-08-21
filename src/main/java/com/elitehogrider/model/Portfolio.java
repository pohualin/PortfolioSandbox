package com.elitehogrider.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Portfolio {

    Map<Ticker, BigDecimal> allocation;

    public Portfolio() {
        allocation = new HashMap<>();
    }

    public Map<Ticker, BigDecimal> getAllocation() {
        return allocation;
    }
}
