package com.elitehogrider.service;

import com.elitehogrider.model.Order;
import com.elitehogrider.model.Portfolio;

import java.util.Calendar;

public interface TradeService {

    void buy(Long traderId, Order order);

    void sell(Long traderId, Order order);

    void updatePortfolioValue(Portfolio portfolio, Calendar updatedOn);

}
