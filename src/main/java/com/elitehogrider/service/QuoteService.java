package com.elitehogrider.service;

import com.elitehogrider.model.indicator.BollingerBandIndicators;
import com.elitehogrider.model.indicator.TwoHundredDaysIndicators;
import com.elitehogrider.model.param.DayOfWeekParam;
import com.elitehogrider.model.param.MovingAverageParam;
import yahoofinance.histquotes.HistoricalQuote;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public interface QuoteService {

    /**
     * Calculate with the past 200 trading days
     */
    List<TwoHundredDaysIndicators> getTwoHundred(String ticker, Calendar from, Calendar to);

    /**
     * Two hundred days indicators are calculated from today -200 to today.
     * With real time data, we only get today -200 to today -1
     * With historical data, we should calculate from dateTo -200 to dateTo -1
     */
    List<TwoHundredDaysIndicators> getTwoHundredDaysIndicators(String ticker, Calendar from, Calendar to);

    List<BollingerBandIndicators> getBollingerBand(String ticker, Calendar from, Calendar to, int days, BigDecimal multiplier);

    List<HistoricalQuote> getDayOfWeek(DayOfWeekParam params);

    /**
     * Moving average with given paramters
     *
     * @param params to be used
     * @return
     */
    Map<Calendar, BigDecimal> getMovingAverage(MovingAverageParam params);
}
