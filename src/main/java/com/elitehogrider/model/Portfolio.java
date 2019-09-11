package com.elitehogrider.model;

import com.elitehogrider.validator.PortfolioAllocateValidator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

public class Portfolio {

    Map<Ticker, BigDecimal> allocation;

    public Portfolio(Map<Ticker, BigDecimal> allocation) {
        PortfolioAllocateValidator.validate(allocation);
        this.allocation = Collections.unmodifiableMap(allocation);
    }

    public Map<Ticker, BigDecimal> getAllocation() {
        return allocation;
    }

}
