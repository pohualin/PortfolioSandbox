package com.elitehogrider.strategy;

import com.elitehogrider.model.Account;
import com.elitehogrider.model.Order;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Signal;
import com.elitehogrider.model.SignalStatus;
import com.elitehogrider.model.SimulateResult;
import com.elitehogrider.model.Trader;
import com.elitehogrider.service.AccountService;
import com.elitehogrider.service.TradeService;
import com.elitehogrider.service.TraderService;
import com.elitehogrider.util.Calculator;
import com.elitehogrider.util.DateUtil;
import com.elitehogrider.validator.OrderValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class AbstractStrategy implements Strategy {

    private static final Logger log = LoggerFactory.getLogger(AbstractStrategy.class);

    @Autowired
    TradeService tradeService;

    @Autowired
    TraderService traderService;

    @Autowired
    AccountService accountService;

    @Override
    public List<Signal> identifySignal(Portfolio portfolio) {
        Calendar from = DateUtil.midnight();
        from.add(Calendar.DATE, -1);
        Calendar today = Calendar.getInstance();
        return this.identifySignal(portfolio, from, today);
    }

    @Override
    public List<Signal> identifySignal(Portfolio portfolio, Calendar from, Calendar to) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Order processSignal(Account account, Signal signal) {
        Order order = new Order(signal.getDate(), signal.getTicker(), signal.getType(),
                signal.getIndicators().getHistoricalQuote().getAdjClose(),
                Calculator.getShares(new BigDecimal(500), signal.getIndicators().getHistoricalQuote().getAdjClose()));
        if (!OrderValidator.isValid(account, order)) {
            switch (signal.getType()) {
                case BUY:
                    signal.setStatus(SignalStatus.DISCARD_BUY);
                    log.debug("Buy Signal discarded: {}", signal);
                    break;
                case SELL:
                    signal.setStatus(SignalStatus.DISCARD_SELL);
                    log.debug("Sell Signal discarded: {}", signal);
                    break;
            }
            return null;
        }
        return order;
    }

    @Override
    public SimulateResult simulate(Long traderId, Calendar from, Calendar to) {
        Trader trader = traderService.getTrader(traderId);
        if (trader == null) {
            throw new RuntimeException("Not an existing trader. Please create a trader.");
        }
        Account account = trader.getAccount();

        List<Signal> signals = identifySignal(account.getPortfolio(), from, to);
        List<Order> orders = new ArrayList<>();
        int[] counts = {0, 0, 0};
        signals.stream().forEach((signal) -> {
            Order orderToProcess = processSignal(account, signal);
            if (orderToProcess != null) {
                switch (orderToProcess.getType()) {
                    case BUY:
                        tradeService.buy(traderId, orderToProcess);
                        signal.setStatus(SignalStatus.BOUGHT);
                        counts[0]++;
                        break;
                    case SELL:
                        tradeService.sell(traderId, orderToProcess);
                        signal.setStatus(SignalStatus.SOLD);
                        counts[1]++;
                        break;
                }
            } else {
                accountService.updateValue(account, signal.getDate());
                counts[2]++;
            }
            orders.add(orderToProcess);
        });

        log.debug("Identified {} trading signal", signals.size());
        log.debug("{} bought {} sold and {} discarded", counts[0], counts[1], counts[2]);

        accountService.updateValue(account, to);

        SimulateResult result = new SimulateResult();
        result.setSignals(signals);
        result.setSummary(
                String.format("Identified %s trading signals. Bought %s, sold %s and discarded %s",
                        signals.size(), counts[0], counts[1], counts[2]));
        result.setAccount(account);

        return result;
    }

}
