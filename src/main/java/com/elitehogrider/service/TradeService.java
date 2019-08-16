package com.elitehogrider.service;

import com.elitehogrider.model.Order;
import com.elitehogrider.model.Trader;

import java.util.List;

public interface TradeService {

    void buy(Long traderId, Order order);

    void sell(Long traderId, Order order);

}
