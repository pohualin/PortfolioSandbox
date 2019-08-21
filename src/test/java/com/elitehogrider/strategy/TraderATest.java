package com.elitehogrider.strategy;

import com.elitehogrider.model.Account;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Ticker;
import com.elitehogrider.model.Trader;
import com.elitehogrider.service.AccountService;
import com.elitehogrider.service.TraderService;
import com.elitehogrider.util.DateUtil;
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
import java.util.Calendar;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class TraderATest {

    private final Logger log = LoggerFactory.getLogger(TraderATest.class);

    Trader traderA;

    @Autowired
    TraderService traderService;

    @Autowired
    TraderAStrategy traderAStrategy;

    @Autowired
    AccountService accountService;

    @Before
    public void init() {
        traderA = traderService.newTrader("George");
        Portfolio portfolio = new Portfolio();
        portfolio.getAllocation().putIfAbsent(Ticker.T, new BigDecimal(100));
        Account account = new Account(new BigDecimal(10000), portfolio);
        traderA.setAccount(account);
    }

    @Test
    public void identifySignal() {
        log.debug("Midnight {}", DateUtil.midnight());
        traderAStrategy.identifySignal(traderA.getAccount().getPortfolio());
        log.debug("Midnight {}", DateUtil.midnight());
    }

    @Test
    public void simulate() {
        Calendar today = Calendar.getInstance();
        Calendar from = (Calendar) today.clone();
        from.add(Calendar.YEAR, -1);
        Calendar to = (Calendar) today.clone();
        to.add(Calendar.MONTH, -1);
        traderAStrategy.simulate(traderA.getId(), from, to);
    }

}