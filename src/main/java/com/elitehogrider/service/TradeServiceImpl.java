package com.elitehogrider.service;

import com.elitehogrider.model.Holding;
import com.elitehogrider.model.Order;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Ticker;
import com.elitehogrider.model.TradeType;
import com.elitehogrider.model.Trader;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TradeServiceImpl implements TradeService {

    @Override
    public Trader buy(Trader trader, Order order) {
        Assert.isTrue(order.getType().equals(TradeType.BUY), "Buy order should have TradeType.BUY");

        BigDecimal cash = trader.getPortfolio().getCash();
        BigDecimal amount = order.getPrice().multiply(order.getShares());

        Portfolio portfolio = trader.getPortfolio();
        portfolio.setCash(cash.subtract(amount));

        Holding holding = new Holding(order.getTicker(), order.getPrice(), order.getShares());
        if (portfolio.getHoldings().containsKey(order.getTicker())) {
            portfolio.getHoldings().get(order.getTicker()).add(holding);
        } else {
            List<Holding> holdings = new ArrayList<>();
            holdings.add(holding);
            portfolio.getHoldings().put(order.getTicker(), holdings);
        }

        return trader;
    }

    @Override
    public Trader sell(Trader trader, Order order) {
        Assert.isTrue(order.getType().equals(TradeType.SELL), "Buy order should have TradeType.SELL");
        Assert.isTrue(trader.getPortfolio().getHoldings().containsKey(order.getTicker()), "Trader has nothing to sell");

        BigDecimal cash = trader.getPortfolio().getCash();
        BigDecimal amount = order.getPrice().multiply(order.getShares());

        trader.getPortfolio().setCash(cash.add(amount));
        List<Holding> holdings = trader.getPortfolio().getHoldings().get(order.getTicker());
        holdings.get(0).setShares(holdings.get(0).getShares().subtract(order.getShares()));
        return trader;
    }
}
