package com.elitehogrider.strategy;

import com.elitehogrider.model.Order;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Signal;
import com.elitehogrider.model.SimulateResult;

import java.util.Calendar;
import java.util.List;

public interface Strategy {

    List<Signal> identifySignal(Portfolio portfolio);

    List<Signal> identifySignal(Portfolio portfolio, Calendar from, Calendar to);

    Order processSignal(Portfolio portfolio, Signal signal);

    SimulateResult simulate(Long traderId, Calendar from, Calendar to);

}
