package com.elitehogrider.service;

import com.elitehogrider.model.Holding;
import com.elitehogrider.model.Order;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Ticker;
import com.elitehogrider.model.TradeType;
import com.elitehogrider.model.Trader;
import com.elitehogrider.validator.OrderValidator;
import com.github.benmanes.caffeine.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TradeServiceImpl implements TradeService {

    private static final Logger log = LoggerFactory.getLogger(TradeServiceImpl.class);

    @Autowired
    TraderService traderService;

    @Override
    public void buy(Long traderId, Order order) {
        Assert.isTrue(order.getType().equals(TradeType.BUY), "Buy order should have TradeType.BUY");

        Trader trader = traderService.getTrader(traderId);
        Portfolio portfolio = trader.getPortfolio();

        OrderValidator.isValid(portfolio, order);

        BigDecimal cash = trader.getPortfolio().getCash();
        BigDecimal amount = order.getPrice().multiply(order.getShares());
        portfolio.setCash(cash.subtract(amount));

        Holding holding = new Holding(order.getTicker(), order.getPrice(), order.getShares());
        if (portfolio.getHoldings().containsKey(order.getTicker())) {
            portfolio.getHoldings().get(order.getTicker()).add(holding);
        } else {
            List<Holding> holdings = new ArrayList<>();
            holdings.add(holding);
            portfolio.getHoldings().put(order.getTicker(), holdings);
        }

        updatePortfolioValue(portfolio);
    }

    @Override
    public void sell(Long traderId, Order order) {
        Assert.isTrue(order.getType().equals(TradeType.SELL), "Buy order should have TradeType.SELL");

        Trader trader = traderService.getTrader(traderId);
        Portfolio portfolio = trader.getPortfolio();

        OrderValidator.isValid(portfolio, order);

        BigDecimal cash = trader.getPortfolio().getCash();
        BigDecimal amount = order.getPrice().multiply(order.getShares());

        portfolio.setCash(cash.add(amount));
        List<Holding> holdings = trader.getPortfolio().getHoldings().get(order.getTicker());
        holdings.get(0).setShares(holdings.get(0).getShares().subtract(order.getShares()));

        updatePortfolioValue(portfolio);
    }

    private void updatePortfolioValue(Portfolio portfolio) {
        BigDecimal stockValue = new BigDecimal(0);
        portfolio.getHoldings().forEach((ticker, holdings) -> {
            try {
                Stock stock = YahooFinance.get(ticker.name());
                BigDecimal shares = holdings.get(0).getShares();
                stockValue.add(stock.getQuote().getPrice().multiply(shares));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        portfolio.setValue(stockValue.add(portfolio.getCash()));
        log.debug("Updated portfolio value: {}", portfolio.getValue());
    }
}
