package com.elitehogrider.config;

import com.elitehogrider.util.Calculator;

import java.math.BigDecimal;

public class Variables {

    public static final int TWO_HUNDRED_DAYS = 200;
    public static final Calculator.StdevType TH_MA_STDEV_TYPE = Calculator.StdevType.SAMPLE;


    public static final int BB_DAYS = 20;
    public static final BigDecimal BB_MULTIPLIER = new BigDecimal(2.1);
    public static final int BB_MAX_DAYS = 50;
    public static final Calculator.StdevType BB_STDEV_TYPE = Calculator.StdevType.POPULATION;

}
