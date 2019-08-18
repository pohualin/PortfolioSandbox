package com.elitehogrider.service;

import com.elitehogrider.model.TwoHundredDaysIndicators;

import java.util.List;

public interface QuoteService {
    List<TwoHundredDaysIndicators> getTwoHundredDaysIndicators(String ticker);
}
