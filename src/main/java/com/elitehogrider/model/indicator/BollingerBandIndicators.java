package com.elitehogrider.model.indicator;

import org.springframework.core.style.ToStringCreator;
import yahoofinance.histquotes.HistoricalQuote;

import java.math.BigDecimal;

public class BollingerBandIndicators extends Indicators {

    BigDecimal average;
    BigDecimal lowerBand;
    BigDecimal upperBand;

    public BollingerBandIndicators(HistoricalQuote historicalQuote, BigDecimal average, BigDecimal stdev, BigDecimal multiplier) {
        super(historicalQuote);
        this.average = average;
        this.lowerBand = average.subtract(stdev.multiply(multiplier)).setScale(2, BigDecimal.ROUND_HALF_UP);
        this.upperBand = average.add(stdev.multiply(multiplier)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getAverage() {
        return average;
    }

    public BigDecimal getLowerBand() {
        return lowerBand;
    }

    public BigDecimal getUpperBand() {
        return upperBand;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("Date", historicalQuote.getDate().getTime())
                .append("Lower Band", lowerBand)
                .append("Average", average)
                .append("Upper Band", upperBand)
                .toString();
    }

}
