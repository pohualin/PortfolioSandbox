package com.elitehogrider.service;

import com.elitehogrider.model.Holding;
import com.elitehogrider.model.Order;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.TradeType;
import com.elitehogrider.model.Trader;
import com.elitehogrider.validator.OrderValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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

        if (!OrderValidator.isValid(portfolio, order)) {
            throw new RuntimeException("Not enough cash to buy");
        }

        BigDecimal cash = trader.getPortfolio().getCash();
        BigDecimal amount = order.getPrice().multiply(order.getShares());
        portfolio.setCash(cash.subtract(amount).setScale(5, BigDecimal.ROUND_HALF_UP));

        if (portfolio.getHoldings().containsKey(order.getTicker())) {
            Holding holding = portfolio.getHoldings().get(order.getTicker()).get(0);
            holding.setShares(holding.getShares().add(order.getShares()));
        } else {
            Holding holding = new Holding(order.getTicker(), order.getPrice(), order.getShares());
            List<Holding> holdings = new ArrayList<>();
            holdings.add(holding);
            portfolio.getHoldings().put(order.getTicker(), holdings);
        }

        log.debug("Bought {} shares of {} at {} on {}",
                order.getShares(), order.getTicker().name(), order.getPrice(), order.getDate().getTime());

        updatePortfolioValue(portfolio, order.getDate());
    }

    @Override
    public void sell(Long traderId, Order order) {
        Assert.isTrue(order.getType().equals(TradeType.SELL), "Sell order should have TradeType.SELL");

        Trader trader = traderService.getTrader(traderId);
        Portfolio portfolio = trader.getPortfolio();

        if (!OrderValidator.isValid(portfolio, order)) {
            throw new RuntimeException("Not enough shares to sell");
        }

        BigDecimal cash = trader.getPortfolio().getCash();
        BigDecimal amount = order.getPrice().multiply(order.getShares());

        portfolio.setCash(cash.add(amount).setScale(5, BigDecimal.ROUND_HALF_UP));
        List<Holding> holdings = trader.getPortfolio().getHoldings().get(order.getTicker());
        holdings.get(0).setShares(holdings.get(0).getShares().subtract(order.getShares()));

        log.debug("Sold {} shares of {} at {} on {}",
                order.getShares(), order.getTicker().name(), order.getPrice(), order.getDate().getTime());
        updatePortfolioValue(portfolio, order.getDate());
    }

    @Override
    public void updatePortfolioValue(Portfolio portfolio, Calendar updatedOn) {
        AtomicReference<BigDecimal> reference = new AtomicReference<>();
        reference.set(new BigDecimal(0));
        portfolio.getHoldings().forEach((ticker, holdings) -> {
            try {
                Stock stock = YahooFinance.get(ticker.name());
                List<HistoricalQuote> quotes = stock.getHistory(updatedOn, updatedOn, Interval.DAILY);
                BigDecimal shares = holdings.get(0).getShares();
                reference.set(reference.get().add(quotes.get(0).getClose().multiply(shares)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        log.debug("Cash: {}", portfolio.getCash());
        portfolio.getHoldings().forEach((ticker, holdings) -> {
            log.debug("{} shares of {}", holdings.get(0).getShares(), ticker.name());
        });
        portfolio.setValue(reference.get().add(portfolio.getCash()).setScale(5, BigDecimal.ROUND_HALF_UP));
        log.debug("Updated portfolio value: {}", portfolio.getValue());
    }
}
