package com.elitehogrider.service;

import com.elitehogrider.model.Trader;

import java.math.BigDecimal;

public interface TraderService {

    Trader newTrader(String name, BigDecimal amounts);

    Trader getTrader(Long id);

}
