package com.elitehogrider.service;

import com.elitehogrider.model.Indicators;
import com.elitehogrider.util.Calculator;
import com.elitehogrider.util.QuoteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuoteServiceImpl implements QuoteService {
    private final Logger log = LoggerFactory.getLogger(QuoteServiceImpl.class);

    @Override
    public List<Indicators> getIndicators(String ticker) throws IOException {
        int daysToFetch = 365;
        int daysToCalculate = 200;

        Calendar today = Calendar.getInstance();
        Calendar from = (Calendar) today.clone();
        from.add(Calendar.DATE, -daysToFetch - daysToCalculate - 1);
        Calendar to = (Calendar) today.clone();
        to.add(Calendar.DATE, -1);

        Stock stock = YahooFinance.get(ticker, from, to, Interval.DAILY);

        List<Indicators> items = new ArrayList<>();
        for (int i = 0; i < stock.getHistory().size(); i++) {
            Calendar dateTo = stock.getHistory().get(i).getDate();
            Calendar dateFrom = (Calendar) dateTo.clone();
            dateFrom.add(Calendar.DATE, -daysToCalculate);

            List<HistoricalQuote> quotes =
                    stock.getHistory().stream()
                            .filter(historicalQuote -> !historicalQuote.getDate().before(dateFrom) && !historicalQuote.getDate().after(dateTo))
                            .collect(Collectors.toList());
            log.debug("Dates: {} ~ {}", dateFrom.getTime(), dateTo.getTime());
            log.debug("History: {}", QuoteUtil.getHistoryCloses(quotes));

            if (quotes.size() > 1)
                items.add(new Indicators(dateTo,
                        Calculator.average(QuoteUtil.getHistoryCloses(quotes)),
                        Calculator.stdev(QuoteUtil.getHistoryCloses(quotes))));
        }
        Collections.sort(items);

        return items;
    }
}

