package com.elitehogrider.service;

import com.elitehogrider.model.Order;
import com.elitehogrider.model.Trader;

public interface TradeService {

    Trader buy(Trader trader, Order order);

    Trader sell(Trader trader, Order order);
}
