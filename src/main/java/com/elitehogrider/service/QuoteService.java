package com.elitehogrider.service;

import com.elitehogrider.model.BollingerBandIndicators;
import com.elitehogrider.model.TwoHundredDaysIndicators;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

public interface QuoteService {
    List<TwoHundredDaysIndicators> getTwoHundredDaysIndicators(String ticker, Calendar from, Calendar to);

    List<BollingerBandIndicators> getBollingerBand(String ticker, Calendar from, Calendar to, int days, BigDecimal multiplier);

    void getFriday();
}
