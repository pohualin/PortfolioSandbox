package com.elitehogrider.service;

import com.elitehogrider.model.TwoHundredDaysIndicators;

import java.util.Calendar;
import java.util.List;

public interface QuoteService {
    List<TwoHundredDaysIndicators> getTwoHundredDaysIndicators(String ticker, Calendar from, Calendar to);
}
