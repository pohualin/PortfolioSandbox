package com.elitehogrider.service;

import com.elitehogrider.model.Holding;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Ticker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private static final Logger log = LoggerFactory.getLogger(PortfolioServiceImpl.class);

    @Override
    public void updateValue(Portfolio portfolio, Calendar updatedOn) {
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

    @Override
    public void addStock(Portfolio portfolio, Ticker ticker) {
        portfolio.getHoldings().putIfAbsent(ticker, new ArrayList<>());
    }

    @Override
    public void addStocks(Portfolio portfolio, List<Ticker> tickerList) {
        tickerList.forEach((ticker -> addStock(portfolio, ticker)));
    }

}
