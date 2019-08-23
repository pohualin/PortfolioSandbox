package com.elitehogrider.strategy;

import com.elitehogrider.model.BollingerBandIndicators;
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
public class BollingerBandStrategy extends AbstractStrategy implements Strategy {

    @Autowired
    QuoteService quoteService;

    @Override
    public List<Signal> identifySignal(Portfolio portfolio, Calendar from, Calendar to) {
        if (portfolio.getAllocation().isEmpty()) {
            throw new RuntimeException("Account contains no stocks");
        }

        List<Signal> signals = new ArrayList<>();

        portfolio.getAllocation().forEach((ticker, percentage) -> {
            List<BollingerBandIndicators> indicatorsList =
                    quoteService.getBollingerBand(ticker.name(), from, to, 20, new BigDecimal(2.1));

            indicatorsList.forEach((indicators -> {
                BigDecimal adjClose = indicators.getHistoricalQuote().getAdjClose();
                HistoricalQuote hq = indicators.getHistoricalQuote();

                if (adjClose.compareTo(indicators.getLowerBand()) == -1) {
                    signals.add(new Signal(hq.getDate(), ticker, TradeType.BUY, indicators, SignalStatus.IDENTIFY_BUY));
                }

                if (adjClose.compareTo(indicators.getUpperBand()) == 1) {
                    signals.add(new Signal(hq.getDate(), ticker, TradeType.SELL, indicators, SignalStatus.IDENTIFY_SELL));
                }

            }));
        });

        return signals;
    }

}
