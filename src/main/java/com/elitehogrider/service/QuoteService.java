package com.elitehogrider.service;

import com.elitehogrider.model.Indicators;

import java.io.IOException;
import java.util.List;

public interface QuoteService {
    List<Indicators> getIndicators(String ticker) throws IOException;
}
