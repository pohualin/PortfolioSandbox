package com.elitehogrider.api;

import com.elitehogrider.model.Account;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Signal;
import com.elitehogrider.model.SimulateResult;
import com.elitehogrider.model.Ticker;
import com.elitehogrider.model.Trader;
import com.elitehogrider.service.AccountService;
import com.elitehogrider.service.TraderService;
import com.elitehogrider.strategy.BollingerBandStrategy;
import com.elitehogrider.strategy.TraderAStrategy;
import com.elitehogrider.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("trading")
public class TradingFloorController {

    private static Logger log = LoggerFactory.getLogger(TradingFloorController.class);

    @Autowired
    TraderService traderService;

    @Autowired
    AccountService accountService;

    @Autowired
    TraderAStrategy traderAStrategy;

    @Autowired
    BollingerBandStrategy bollingerBandStrategy;

    @RequestMapping("traderA/simulate/{from}/{to}")
    public SimulateResult simulateTraderA(@PathVariable String from, @PathVariable String to) {
        Trader trader = initTrader();
        return traderAStrategy.simulate(trader.getId(), DateUtil.parseDateString(from), DateUtil.parseDateString(to));
    }

    @RequestMapping("traderA/identifySignal")
    public List<Signal> identifyTraderASignal() {
        Portfolio portfolio = new Portfolio(new HashMap<>());
        portfolio.getAllocation().putIfAbsent(Ticker.T, new BigDecimal(100));
        portfolio.getAllocation().putIfAbsent(Ticker.VZ, new BigDecimal(100));
        portfolio.getAllocation().putIfAbsent(Ticker.VTI, new BigDecimal(100));
        portfolio.getAllocation().putIfAbsent(Ticker.VNQ, new BigDecimal(100));
        portfolio.getAllocation().putIfAbsent(Ticker.BND, new BigDecimal(100));
        portfolio.getAllocation().putIfAbsent(Ticker.FLRN, new BigDecimal(100));
        return traderAStrategy.identifySignal(portfolio);
    }

    @RequestMapping("bollingerBand/simulate/{from}/{to}")
    public SimulateResult simulateBollingerBand(@PathVariable String from, @PathVariable String to) {
        Trader trader = initTrader();
        return bollingerBandStrategy.simulate(trader.getId(), DateUtil.parseDateString(from), DateUtil.parseDateString(to));
    }

    private Trader initTrader() {
        Trader trader = traderService.newTrader("George");
        Portfolio portfolio = new Portfolio(new HashMap<>());
        portfolio.getAllocation().putIfAbsent(Ticker.VTI, new BigDecimal(100));
        Account account = new Account(new BigDecimal(10000), portfolio);
        trader.setAccount(account);
        return trader;
    }

}