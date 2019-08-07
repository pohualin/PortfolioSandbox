package com.elitehogrider.service;

import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;

@Service
public class QuoteServiceImpl implements QuoteService {

    public Stock getStock(String ticker) {
        Stock stock = null;
        try {
            stock = YahooFinance.get(ticker, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stock;
    }
}

