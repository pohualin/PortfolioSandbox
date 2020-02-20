package com.elitehogrider.service;

import com.elitehogrider.model.Account;
import com.elitehogrider.model.Order;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Ticker;
import com.elitehogrider.model.TradeType;
import com.elitehogrider.model.Trader;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class TradeServiceTest {

    private final Logger log = LoggerFactory.getLogger(TradeServiceTest.class);

    @Autowired
    TraderService traderService;

    @Autowired
    TradeService tradeService;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void buy() {
        Trader george = traderService.newTrader("George");
        Map<Ticker, BigDecimal> allocation = new HashMap<>();
        allocation.putIfAbsent(Ticker.T, new BigDecimal(100));
        Portfolio portfolio = new Portfolio(allocation);
        Account account = new Account(new BigDecimal(10000), portfolio);
        george.setAccount(account);

        Order buy = new Order(Calendar.getInstance(), Ticker.T, TradeType.BUY, new BigDecimal(30), new BigDecimal(100));
        tradeService.buy(george.getId(), buy);

        george = traderService.getTrader(george.getId());

        Assert.assertTrue(george.getAccount().getCash().equals(new BigDecimal(7000).setScale(2)));
        Assert.assertTrue(george.getAccount().getHoldingByTicker(Ticker.T).get(0).getPrice().equals(new BigDecimal(30)));
        Assert.assertTrue(george.getAccount().getHoldingByTicker(Ticker.T).get(0).getShares().equals(new BigDecimal(100)));
        Assert.assertTrue(george.getAccount().getHoldingByTicker(Ticker.T).get(0).getTicker().equals(Ticker.T));
    }

    @Test
    public void buyWithoutMoney() throws Exception {
        Trader george = traderService.newTrader("George");
        Map<Ticker, BigDecimal> allocation = new HashMap<>();
        allocation.putIfAbsent(Ticker.T, new BigDecimal(100));
        Portfolio portfolio = new Portfolio(allocation);
        Account account = new Account(BigDecimal.ZERO, portfolio);
        george.setAccount(account);


        Order buy = new Order(Calendar.getInstance(), Ticker.T, TradeType.BUY, new BigDecimal(30), new BigDecimal(100));

        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Not enough cash to buy");

        tradeService.buy(george.getId(), buy);
    }

    @Test
    public void sell() {
        Trader george = traderService.newTrader("George");
        Map<Ticker, BigDecimal> allocation = new HashMap<>();
        allocation.putIfAbsent(Ticker.T, new BigDecimal(100));
        Portfolio portfolio = new Portfolio(allocation);
        Account account = new Account(new BigDecimal(10000), portfolio);
        george.setAccount(account);

        Order buy = new Order(Calendar.getInstance(), Ticker.T, TradeType.BUY, new BigDecimal(30), new BigDecimal(100));
        tradeService.buy(george.getId(), buy);

        Order sell = new Order(Calendar.getInstance(), Ticker.T, TradeType.SELL, new BigDecimal(30), new BigDecimal(50));
        tradeService.sell(george.getId(), sell);

        george = traderService.getTrader(george.getId());

        Assert.assertTrue(george.getAccount().getCash().equals(new BigDecimal(8500).setScale(2)));
        Assert.assertTrue(george.getAccount().getHoldingByTicker(Ticker.T).get(0).getShares().equals(new BigDecimal(50)));
        Assert.assertTrue(george.getAccount().getHoldingByTicker(Ticker.T).get(0).getTicker().equals(Ticker.T));
    }

    @Test
    public void sellWithoutShares() throws Exception {
        Trader george = traderService.newTrader("George");
        Map<Ticker, BigDecimal> allocation = new HashMap<>();
        allocation.putIfAbsent(Ticker.T, new BigDecimal(100));
        Portfolio portfolio = new Portfolio(allocation);
        Account account = new Account(new BigDecimal(10000), portfolio);
        george.setAccount(account);

        Order buy = new Order(Calendar.getInstance(), Ticker.T, TradeType.BUY, new BigDecimal(30), new BigDecimal(100));
        tradeService.buy(george.getId(), buy);

        Order sell = new Order(Calendar.getInstance(), Ticker.T, TradeType.SELL, new BigDecimal(30), new BigDecimal(100));
        tradeService.sell(george.getId(), sell);

        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Not enough shares to sell");

        Order sellA = new Order(Calendar.getInstance(), Ticker.T, TradeType.SELL, new BigDecimal(30), new BigDecimal(1));
        tradeService.sell(george.getId(), sellA);

    }

}