package com.elitehogrider.strategy;

import com.elitehogrider.model.Account;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Ticker;
import com.elitehogrider.model.Trader;
import com.elitehogrider.service.AccountService;
import com.elitehogrider.service.TraderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class AbstractStrategyTest {

    private final Logger log = LoggerFactory.getLogger(AbstractStrategyTest.class);

    Trader trader;

    @Autowired
    TraderService traderService;

    @Autowired
    BollingerBandStrategy bollingerBandStrategy;

    @Autowired
    AccountService accountService;

    @Before
    public void init() {
        trader = traderService.newTrader("George");
        Map<Ticker, BigDecimal> allocation = new HashMap<>();
        allocation.putIfAbsent(Ticker.T, new BigDecimal(100.00));
        Portfolio portfolio = new Portfolio(allocation);
        Account account = new Account(new BigDecimal(10000), portfolio);
        trader.setAccount(account);
    }

    @Test
    public void dummy() {

    }

}