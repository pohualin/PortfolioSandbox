package com.elitehogrider.service;

import com.elitehogrider.model.Trader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class TraderServiceTest {

    private final Logger log = LoggerFactory.getLogger(TraderServiceTest.class);

    @Autowired
    TraderService traderService;

    @Test
    public void newTrader() {
        Trader trader = traderService.newTrader("George");
        Assert.assertTrue(trader.getId() != null);
    }

    @Test
    public void getTrader() {
        Trader trader = traderService.newTrader("George");
        Assert.assertTrue(trader.getId() != null);

        Trader created = traderService.getTrader(trader.getId());
        Assert.assertTrue(trader.getId().equals(created.getId()));
    }

    @Test
    public void getUnknownTrader() {
        Trader created = traderService.getTrader(1l);
        Assert.assertTrue(created == null);
    }

}