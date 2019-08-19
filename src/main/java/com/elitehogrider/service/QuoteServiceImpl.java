package com.elitehogrider.service;

import com.elitehogrider.model.TwoHundredDaysIndicators;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuoteServiceImpl implements QuoteService {
    private final Logger log = LoggerFactory.getLogger(QuoteServiceImpl.class);

    private final int TWO_HUNDRED_DAYS = 200;

    /**
     * Two hundred days indicators are calculated from today -200 to today.
     * With real time data, we only get today -200 to today -1
     * With historical data, we should calculate from dateTo -200 to dateTo -1
     *
     * @param ticker
     * @return
     */
    @Override
    public List<TwoHundredDaysIndicators> getTwoHundredDaysIndicators(String ticker) {
        int daysToFetch = 365;

        // 200 days indicators
        Calendar today = Calendar.getInstance();

        Calendar from = (Calendar) today.clone();
        from.add(Calendar.DATE, -daysToFetch);
        Calendar fetchFrom = (Calendar) today.clone();
        fetchFrom.add(Calendar.DATE, -daysToFetch - TWO_HUNDRED_DAYS);
        Calendar to = (Calendar) today.clone();

        Stock stock;
        List<TwoHundredDaysIndicators> items = new ArrayList<>();
        try {
            stock = YahooFinance.get(ticker, fetchFrom, to, Interval.DAILY);

            // Adding today when there is no HistoricalQuote for today (at trading hours)
            LocalDateTime now = LocalDateTime.now();
            Instant startOfDay = now.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
            Calendar midnight = Calendar.getInstance();
            midnight.setTime(Date.from(startOfDay));

            List<HistoricalQuote> filtered = stock.getHistory().stream()
                    .filter(historicalQuote -> historicalQuote.getDate().equals(midnight))
                    .collect(Collectors.toList());

            if (filtered.isEmpty()) {
                HistoricalQuote present = new HistoricalQuote();
                present.setDate(midnight);
                present.setClose(stock.getQuote().getPrice());
                stock.getHistory().add(present);
            }

            for (int i = 0; i < stock.getHistory().size(); i++) {
                HistoricalQuote historicalQuote = stock.getHistory().get(i);
                Calendar hqDate = historicalQuote.getDate();
                Calendar dateTo = (Calendar) hqDate.clone();
                dateTo.add(Calendar.DATE, -1);
                Calendar dateFrom = (Calendar) hqDate.clone();
                dateFrom.add(Calendar.DATE, -TWO_HUNDRED_DAYS);

                if (!dateTo.before(from)) {
                    List<HistoricalQuote> quotes =
                            stock.getHistory().stream()
                                    .filter(hq -> !hq.getDate().before(dateFrom) && !hq.getDate().after(dateTo))
                                    .collect(Collectors.toList());

                    log.debug("Dates: {} ~ {}", dateFrom.getTime(), dateTo.getTime());
                    log.debug("History: {}", QuoteUtil.getHistoryCloses(quotes));

                    if (quotes.size() > 1)
                        items.add(new TwoHundredDaysIndicators(historicalQuote,
                                Calculator.average(QuoteUtil.getHistoryCloses(quotes)),
                                Calculator.stdev(QuoteUtil.getHistoryCloses(quotes))));
                }
            }
            Collections.sort(items);
        } catch (IOException e) {
            throw new RuntimeException();
        }

        return items;
    }

}

