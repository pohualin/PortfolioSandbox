package com.elitehogrider.service;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;

public class QuoteServiceImpl implements QuoteService {

    public Stock getStock(String ticker) {
        Stock stock = null;
        try {
            stock = YahooFinance.get("T");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stock;
    }
}

