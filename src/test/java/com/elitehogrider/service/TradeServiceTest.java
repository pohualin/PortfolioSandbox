package com.elitehogrider.service;

import com.elitehogrider.model.Order;
import com.elitehogrider.model.Ticker;
import com.elitehogrider.model.TradeType;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class TradeServiceTest {

    private final Logger log = LoggerFactory.getLogger(TradeServiceTest.class);

    @Autowired
    TraderService traderService;

    @Autowired
    TradeService tradeService;

    @Test
    public void buy() throws IOException {
        Trader george = traderService.newTrader("George", new BigDecimal(10000));
        Order buy = new Order(Calendar.getInstance(), Ticker.T, TradeType.BUY, new BigDecimal(30), new BigDecimal(100));
        tradeService.buy(george.getId(), buy);

        george = traderService.getTrader(george.getId());

        Assert.assertTrue(george.getPortfolio().getCash().equals(new BigDecimal(7000)));
        Assert.assertTrue(george.getPortfolio().getHoldings().get(Ticker.T).get(0).getPrice().equals(new BigDecimal(30)));
        Assert.assertTrue(george.getPortfolio().getHoldings().get(Ticker.T).get(0).getShares().equals(new BigDecimal(100)));
        Assert.assertTrue(george.getPortfolio().getHoldings().get(Ticker.T).get(0).getTicker().equals(Ticker.T));
    }

    @Test
    public void sell() throws IOException {
        Trader george = traderService.newTrader("George", new BigDecimal(10000));
        Order buy = new Order(Calendar.getInstance(), Ticker.T, TradeType.BUY, new BigDecimal(30), new BigDecimal(100));
        tradeService.buy(george.getId(), buy);

        log.debug("{} shares", george.getPortfolio().getHoldings().get(Ticker.T).get(0).getShares());

        Order sell = new Order(Calendar.getInstance(), Ticker.T, TradeType.SELL, new BigDecimal(30), new BigDecimal(50));
        tradeService.sell(george.getId(), sell);

        log.debug("{} shares", george.getPortfolio().getHoldings().get(Ticker.T).get(0).getShares());

        george = traderService.getTrader(george.getId());

        Assert.assertTrue(george.getPortfolio().getCash().equals(new BigDecimal(8500)));
        Assert.assertTrue(george.getPortfolio().getHoldings().get(Ticker.T).get(0).getShares().equals(new BigDecimal(50)));
        Assert.assertTrue(george.getPortfolio().getHoldings().get(Ticker.T).get(0).getTicker().equals(Ticker.T));
    }

}