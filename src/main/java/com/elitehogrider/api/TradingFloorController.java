package com.elitehogrider.api;

import com.elitehogrider.model.Signal;
import com.elitehogrider.model.Ticker;
import com.elitehogrider.model.SimulateResult;
import com.elitehogrider.model.Trader;
import com.elitehogrider.service.PortfolioService;
import com.elitehogrider.service.TraderService;
import com.elitehogrider.strategy.TraderAStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("trading")
public class TradingFloorController {

    private static Logger log = LoggerFactory.getLogger(TradingFloorController.class);

    @Autowired
    TraderService traderService;

    @Autowired
    PortfolioService portfolioService;

    @Autowired
    TraderAStrategy traderAStrategy;

    @RequestMapping("traderA/simulate")
    public SimulateResult simulateTraderA() {
        Trader traderA = traderService.newTrader("George", new BigDecimal(10000));
        portfolioService.addStocks(traderA.getPortfolio(), Arrays.asList(Ticker.T));

        Calendar today = Calendar.getInstance();
        Calendar from = (Calendar) today.clone();
        from.add(Calendar.YEAR, -1);
        Calendar to = (Calendar) today.clone();

        return traderAStrategy.simulate(traderA.getId(), from, to);
    }

    @RequestMapping("traderA/identifySignal")
    public List<Signal> identifyTraderASignal() {
        Trader traderA = traderService.newTrader("George", new BigDecimal(10000));
        portfolioService.addStocks(traderA.getPortfolio(), Arrays.asList(Ticker.T));
        return traderAStrategy.identifySignal(traderA.getPortfolio());
    }

}