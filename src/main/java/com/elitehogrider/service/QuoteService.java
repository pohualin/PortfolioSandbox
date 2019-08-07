package com.elitehogrider.service;

import yahoofinance.Stock;

public interface QuoteService {

    Stock getStock(String ticker);
}
