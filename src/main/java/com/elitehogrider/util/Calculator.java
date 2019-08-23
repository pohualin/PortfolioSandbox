package com.elitehogrider.util;

import java.math.BigDecimal;
import java.util.List;

public final class Calculator {

    private Calculator() {
    }

    public static BigDecimal average(List<BigDecimal> series) {
        double avg = series.stream()
                .mapToDouble(BigDecimal::doubleValue).average().orElse(Double.NaN);
        return BigDecimal.valueOf(avg).setScale(5, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal stdev(List<BigDecimal> series, StdevType type) {
        BigDecimal mean = average(series);

        Double sum = series.stream()
                .mapToDouble(value -> Math.pow(Math.abs(value.doubleValue() - mean.doubleValue()), 2))
                .sum();

        BigDecimal stdev = new BigDecimal(0);
        switch (type) {
            case POPULATION:
                stdev = BigDecimal.valueOf(
                        Math.sqrt(sum / (series.size()))).setScale(5, BigDecimal.ROUND_HALF_UP);
                break;
            case SAMPLE:
                stdev = BigDecimal.valueOf(
                        Math.sqrt(sum / (series.size() - 1))).setScale(5, BigDecimal.ROUND_HALF_UP);
                break;
        }
        return stdev;
    }

    public enum StdevType {
        POPULATION,
        SAMPLE;
    }

    public static BigDecimal getShares(BigDecimal amount, BigDecimal price) {
        return amount.divide(price, 5, BigDecimal.ROUND_HALF_UP);
    }

}
