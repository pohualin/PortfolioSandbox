package com.elitehogrider.service;

import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Ticker;
import com.elitehogrider.model.Trader;

import java.math.BigDecimal;
import java.util.List;

public interface TraderService {

    Trader newTrader(String name, BigDecimal amounts);

    Trader getTrader(Long id);

}
