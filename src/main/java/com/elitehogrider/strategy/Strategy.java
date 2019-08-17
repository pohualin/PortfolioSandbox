package com.elitehogrider.strategy;

import com.elitehogrider.model.Order;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Signal;

import javax.sound.sampled.Port;
import java.util.List;

public interface Strategy {

    List<Signal> identifySignal();

    Order processSignal(Portfolio portfolio, Signal signal);

    void execute(Long traderId);

}
