package com.elitehogrider.service;

import com.elitehogrider.model.Trader;

import java.math.BigDecimal;

public interface TraderService {

    Trader newTrader(String name);

    Trader getTrader(Long id);

}
