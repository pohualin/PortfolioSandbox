package com.elitehogrider.strategy;

import com.elitehogrider.model.Order;

import java.util.List;

public interface Strategy {

    List<Order> identifySignal();

    List<Order> generateOrders(Long traderId);

    void execute(Long traderId);

}
