package com.elitehogrider.strategy;

import com.elitehogrider.model.Indicators;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Signal;
import com.elitehogrider.model.Ticker;
import com.elitehogrider.model.TradeType;
import com.elitehogrider.model.TwoHundredDaysIndicators;
import com.elitehogrider.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yahoofinance.histquotes.HistoricalQuote;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TraderAStrategy extends AbstractStrategy implements Strategy {

    @Autowired
    QuoteService quoteService;

    @Override
    public List<Signal> identifySignal(Portfolio portfolio) {
        List<TwoHundredDaysIndicators> indicatorsList = quoteService.getTwoHundredDaysIndicators(Ticker.T.name());

        List<Signal> signals = new ArrayList<>();
        indicatorsList.forEach((indicators -> {
            BigDecimal close = indicators.getHistoricalQuote().getClose();
            HistoricalQuote hq = indicators.getHistoricalQuote();

            if (close.compareTo(indicators.getMeanMinusTwoStdev()) == -1) {
                signals.add(new Signal(hq.getDate(), Ticker.T, TradeType.BUY, indicators));
            }

            if (close.compareTo(indicators.getMeanAddTwoStdev()) == 1) {
                signals.add(new Signal(hq.getDate(), Ticker.T, TradeType.SELL, indicators));
            }

        }));

        return signals;
    }

}
