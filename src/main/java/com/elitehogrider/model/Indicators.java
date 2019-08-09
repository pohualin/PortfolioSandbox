package com.elitehogrider.model;

import org.springframework.core.style.ToStringCreator;

import java.math.BigDecimal;
import java.util.Calendar;

public class Indicators implements Comparable<Indicators> {

    Calendar date;
    BigDecimal mean;
    BigDecimal meanMinusOneStdev;
    BigDecimal meanMinusTwoStdev;
    BigDecimal meanAddOneStdev;
    BigDecimal meanAddTwoStdev;

    public Indicators(Calendar date, BigDecimal mean, BigDecimal stdev) {
        this.date = date;
        this.mean = mean;
        this.meanMinusOneStdev = mean.subtract(stdev);
        this.meanMinusTwoStdev = mean.subtract(stdev.multiply(new BigDecimal(2)));
        this.meanAddOneStdev = mean.add(stdev);
        this.meanAddTwoStdev = mean.add(stdev.multiply(new BigDecimal(2)));
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("Date", date.getTime())
                .append("-2STDEV", meanMinusTwoStdev)
                .append("-STDEV", meanMinusOneStdev)
                .append("Average", mean)
                .append("+STDEV", meanAddOneStdev)
                .append("+2STDEV", meanAddTwoStdev)
                .toString();
    }

    @Override
    public int compareTo(Indicators o) {
        return this.date.compareTo(o.date);
    }
}
