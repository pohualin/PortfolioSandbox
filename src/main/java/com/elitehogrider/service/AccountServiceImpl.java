package com.elitehogrider.service;

import com.elitehogrider.model.Account;
import com.elitehogrider.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Override
    public void updateValue(Account account, Calendar updatedOn) {
        AtomicReference<BigDecimal> reference = new AtomicReference<>();
        reference.set(new BigDecimal(0));
        account.getHoldings().forEach((holding) -> {
            try {
                Stock stock = YahooFinance.get(holding.getTicker().name());
                BigDecimal shares = holding.getShares();

                Calendar midnight = DateUtil.midnight();

                if (updatedOn.before(midnight)) {
                    Calendar buffer = (Calendar) updatedOn.clone();
                    buffer.add(Calendar.DATE, -5);
                    List<HistoricalQuote> quotes = stock.getHistory(buffer, updatedOn, Interval.DAILY);
                    reference.set(reference.get().add(quotes.get(quotes.size() - 1).getAdjClose().multiply(shares)));
                } else {
                    reference.set(reference.get().add(stock.getQuote().getPrice().multiply(shares)));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        log.debug("Cash: {}", account.getCash());
        account.getHoldings().forEach((holding) -> {
            log.debug("{} shares of {}", holding.getShares(), holding.getTicker().name());
        });
        account.setValue(reference.get().add(account.getCash()).setScale(5, BigDecimal.ROUND_HALF_UP));
        log.debug("Account value {} on {}", account.getValue(), updatedOn.getTime());
    }

}
