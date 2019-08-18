package com.elitehogrider.strategy;

import com.elitehogrider.model.Order;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Signal;

import java.util.List;

public interface Strategy {

    List<Signal> identifySignal(Portfolio portfolio);

    Order processSignal(Portfolio portfolio, Signal signal);

    void execute(Long traderId);

}
