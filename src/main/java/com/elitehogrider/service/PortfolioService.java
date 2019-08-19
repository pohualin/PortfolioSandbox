package com.elitehogrider.service;

import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Ticker;

import java.util.Calendar;
import java.util.List;

public interface PortfolioService {

    void updateValue(Portfolio portfolio, Calendar updatedOn);

    void addStock(Portfolio portfolio, Ticker ticker);

    void addStocks(Portfolio portfolio, List<Ticker> tickerList);
}
