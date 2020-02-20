package com.elitehogrider.service;

import com.elitehogrider.model.Account;
import com.elitehogrider.model.Holding;
import com.elitehogrider.model.Order;
import com.elitehogrider.model.TradeType;
import com.elitehogrider.model.Trader;
import com.elitehogrider.validator.OrderValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
@Service
public class TradeServiceImpl implements TradeService {

    private static final Logger log = LoggerFactory.getLogger(TradeServiceImpl.class);

    @Autowired
    TraderService traderService;

    @Autowired
    AccountService accountService;

    @Override
    public void buy(Long traderId, Order order) {
        Assert.isTrue(order.getType().equals(TradeType.BUY), "Buy order should have TradeType.BUY");

        Trader trader = traderService.getTrader(traderId);
        Account account = trader.getAccount();

        if (!OrderValidator.isValid(account, order)) {
            throw new RuntimeException("Not enough cash to buy");
        }

        BigDecimal cash = trader.getAccount().getCash();
        BigDecimal amount = order.getPrice().multiply(order.getShares());
        account.setCash(cash.subtract(amount).setScale(2, BigDecimal.ROUND_HALF_UP));

        Holding inbound = new Holding(order.getTicker(), order.getPrice(), order.getShares());

        List<Holding> existing = account.getHoldingByTicker(order.getTicker());
        if (!existing.isEmpty()) {
            Holding holding = existing.get(0);
            holding.setShares(holding.getShares().add(inbound.getShares()));
        } else {
            account.getHoldings().add(inbound);
        }

        log.debug("Bought {} shares of {} at {} on {}",
                order.getShares(), order.getTicker().name(), order.getPrice(), order.getDate().getTime());

        accountService.updateValue(account, order.getDate());
    }

    @Override
    public void sell(Long traderId, Order order) {
        Assert.isTrue(order.getType().equals(TradeType.SELL), "Sell order should have TradeType.SELL");

        Trader trader = traderService.getTrader(traderId);
        Account account = trader.getAccount();

        if (!OrderValidator.isValid(account, order)) {
            throw new RuntimeException("Not enough shares to sell");
        }

        BigDecimal cash = trader.getAccount().getCash();
        BigDecimal amount = order.getPrice().multiply(order.getShares());

        account.setCash(cash.add(amount).setScale(2, BigDecimal.ROUND_HALF_UP));
        List<Holding> holdings = trader.getAccount().getHoldingByTicker(order.getTicker());
        holdings.get(0).setShares(holdings.get(0).getShares().subtract(order.getShares()));

        log.debug("Sold {} shares of {} at {} on {}",
                order.getShares(), order.getTicker().name(), order.getPrice(), order.getDate().getTime());
        accountService.updateValue(account, order.getDate());
    }

}
