package com.elitehogrider.service;

import com.elitehogrider.model.Order;

public interface TradeService {

    void buy(Long traderId, Order order);

    void sell(Long traderId, Order order);

}
