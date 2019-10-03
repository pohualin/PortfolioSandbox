package com.elitehogrider.validator;

import com.elitehogrider.model.Portfolio;

import java.math.BigDecimal;
import java.util.stream.Collectors;

public class PortfolioValidator {

    /**
     * Allocation can not be negative or zero
     * Total allocation must equal or less than 100
     */
    public static void validate(Portfolio portfolio) {
        if (portfolio == null || portfolio.getAllocation() == null) {
            throw new IllegalArgumentException(ValidationMessages.INVALID_PORTFOLIO);
        }

        if (portfolio.getAllocation().values().stream()
                .filter(v -> v.compareTo(BigDecimal.ZERO) < 1).collect(Collectors.toList()).size() > 0) {
            throw new IllegalArgumentException(ValidationMessages.INVALID_PORTFOLIO);
        }

        if (portfolio.getAllocation().values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP)
                .compareTo(new BigDecimal(100.00)) != 0) {
            throw new IllegalArgumentException(ValidationMessages.INVALID_PORTFOLIO);
        }
    }
}
