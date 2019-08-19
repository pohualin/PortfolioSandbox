package com.elitehogrider.strategy;

import com.elitehogrider.model.Order;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Signal;
import com.elitehogrider.model.Trader;
import com.elitehogrider.service.PortfolioService;
import com.elitehogrider.service.TradeService;
import com.elitehogrider.service.TraderService;
import com.elitehogrider.util.Calculator;
import com.elitehogrider.validator.OrderValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class AbstractStrategy implements Strategy {

    private static final Logger log = LoggerFactory.getLogger(AbstractStrategy.class);

    @Autowired
    TradeService tradeService;

    @Autowired
    TraderService traderService;

    @Autowired
    PortfolioService portfolioService;

    @Override
    public List<Signal> identifySignal(Portfolio portfolio) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Order processSignal(Portfolio portfolio, Signal signal) {
        Order order = new Order(signal.getDate(), signal.getTicker(), signal.getType(),
                signal.getIndicators().getHistoricalQuote().getClose(),
                Calculator.getShares(new BigDecimal(500), signal.getIndicators().getHistoricalQuote().getClose()));
        if (!OrderValidator.isValid(portfolio, order)) {
            log.debug("Signal discarded: {}", signal);
            return null;
        }
        return order;
    }

    @Override
    public void execute(Long traderId) {
        Trader trader = traderService.getTrader(traderId);
        if (trader == null) {
            throw new RuntimeException("Not an existing trader. Please create a trader.");
        }
        Portfolio portfolio = trader.getPortfolio();

        List<Signal> signals = identifySignal(portfolio);
        int[] counts = {0, 0, 0};
        signals.stream().forEach((signal) -> {
            Order orderToProcess = processSignal(portfolio, signal);
            if (orderToProcess != null) {
                switch (orderToProcess.getType()) {
                    case BUY:
                        tradeService.buy(traderId, orderToProcess);
                        counts[0]++;
                        break;
                    case SELL:
                        tradeService.sell(traderId, orderToProcess);
                        counts[1]++;
                        break;
                }
            } else {
                counts[2]++;
                portfolioService.updateValue(portfolio, signal.getDate());
            }
        });

        log.debug("Identified {} trading signal", signals.size());
        log.debug("{} bought {} sold and {} discarded", counts[0], counts[1], counts[2]);
    }
}
