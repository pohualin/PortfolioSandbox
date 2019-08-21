package com.elitehogrider.service;

import com.elitehogrider.model.Trader;
import com.github.benmanes.caffeine.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TraderServiceImpl implements TraderService {

    private static final Logger log = LoggerFactory.getLogger(TraderServiceImpl.class);

    @Autowired
    Cache<Long, Trader> traderCache;

    @Override
    public Trader newTrader(String name) {
        Trader trader = new Trader(name);
        traderCache.put(trader.getId(), trader);
        return traderCache.getIfPresent(trader.getId());
    }

    @Override
    public Trader getTrader(Long id) {
        return traderCache.getIfPresent(id);
    }

}
