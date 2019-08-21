package com.elitehogrider.strategy;

import com.elitehogrider.model.Account;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Signal;
import com.elitehogrider.model.SignalStatus;
import com.elitehogrider.model.TradeType;
import com.elitehogrider.model.TwoHundredDaysIndicators;
import com.elitehogrider.service.QuoteService;
import com.elitehogrider.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yahoofinance.histquotes.HistoricalQuote;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class TraderAStrategy extends AbstractStrategy implements Strategy {

    @Autowired
    QuoteService quoteService;

    @Override
    public List<Signal> identifySignal(Portfolio portfolio) {
        Calendar from = DateUtil.midnight();
        from.add(Calendar.DATE, -1);
        Calendar today = Calendar.getInstance();
        return this.identifySignal(portfolio, from, today);
    }

    @Override
    public List<Signal> identifySignal(Portfolio portfolio, Calendar from, Calendar to) {
        if (portfolio.getAllocation().isEmpty()) {
            throw new RuntimeException("Account contains no stocks");
        }

        List<Signal> signals = new ArrayList<>();

        portfolio.getAllocation().forEach((ticker, percentage) -> {
            List<TwoHundredDaysIndicators> indicatorsList = quoteService.getTwoHundredDaysIndicators(ticker.name(), from, to);

            indicatorsList.forEach((indicators -> {
                BigDecimal close = indicators.getHistoricalQuote().getClose();
                HistoricalQuote hq = indicators.getHistoricalQuote();

                if (close.compareTo(indicators.getMeanMinusTwoStdev()) == -1) {
                    signals.add(new Signal(hq.getDate(), ticker, TradeType.BUY, indicators, SignalStatus.IDENTIFIED));
                }

                if (close.compareTo(indicators.getMeanAddTwoStdev()) == 1) {
                    signals.add(new Signal(hq.getDate(), ticker, TradeType.SELL, indicators, SignalStatus.IDENTIFIED));
                }

            }));
        });

        return signals;
    }

}
