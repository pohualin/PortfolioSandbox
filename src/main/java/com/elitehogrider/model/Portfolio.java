package com.elitehogrider.model;

import com.elitehogrider.validator.PortfolioValidator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

public class Portfolio {

    Map<Ticker, BigDecimal> allocation;

    public Portfolio(Map<Ticker, BigDecimal> allocation) {
        this.allocation = Collections.unmodifiableMap(allocation);
        PortfolioValidator.validate(this);
    }

    public Map<Ticker, BigDecimal> getAllocation() {
        return allocation;
    }
}
