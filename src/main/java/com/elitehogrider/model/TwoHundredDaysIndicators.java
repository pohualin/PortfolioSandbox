package com.elitehogrider.model;

import org.springframework.core.style.ToStringCreator;
import yahoofinance.histquotes.HistoricalQuote;

import java.math.BigDecimal;

public class TwoHundredDaysIndicators extends Indicators {

    BigDecimal mean;
    BigDecimal meanMinusOneStdev;
    BigDecimal meanMinusTwoStdev;
    BigDecimal meanAddOneStdev;
    BigDecimal meanAddTwoStdev;

    public TwoHundredDaysIndicators(HistoricalQuote historicalQuote, BigDecimal mean, BigDecimal stdev) {
        super(historicalQuote);
        this.mean = mean;
        this.meanMinusOneStdev = mean.subtract(stdev);
        this.meanMinusTwoStdev = mean.subtract(stdev.multiply(new BigDecimal(2)));
        this.meanAddOneStdev = mean.add(stdev);
        this.meanAddTwoStdev = mean.add(stdev.multiply(new BigDecimal(2)));
    }

    public BigDecimal getMean() {
        return mean;
    }

    public BigDecimal getMeanMinusTwoStdev() {
        return meanMinusTwoStdev;
    }

    public BigDecimal getMeanAddOneStdev() {
        return meanAddOneStdev;
    }

    public BigDecimal getMeanAddTwoStdev() {
        return meanAddTwoStdev;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("Date", historicalQuote.getDate().getTime())
                .append("-2STDEV", meanMinusTwoStdev)
                .append("-STDEV", meanMinusOneStdev)
                .append("Average", mean)
                .append("+STDEV", meanAddOneStdev)
                .append("+2STDEV", meanAddTwoStdev)
                .toString();
    }

}
