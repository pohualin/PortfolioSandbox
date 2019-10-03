package com.elitehogrider.service;

import com.elitehogrider.config.Variables;
import com.elitehogrider.model.BollingerBandIndicators;
import com.elitehogrider.model.TwoHundredDaysIndicators;
import com.elitehogrider.util.Calculator;
import com.elitehogrider.util.DateUtil;
import com.elitehogrider.util.QuoteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuoteServiceImpl implements QuoteService {
    private final Logger log = LoggerFactory.getLogger(QuoteServiceImpl.class);

    @Override
    public List<TwoHundredDaysIndicators> getTwoHundred(String ticker, Calendar from, Calendar to) {
        Calendar fetchFrom = (Calendar) from.clone();
        fetchFrom.add(Calendar.DATE, -Variables.THREE_HUNDRED_TWENTY_DAYS);

        Stock stock;
        List<TwoHundredDaysIndicators> items = new ArrayList<>();
        try {
            stock = YahooFinance.get(ticker, fetchFrom, to, Interval.DAILY);

            _addTodayHistoricalQuote(stock, to);
            
            for (int i = 0; i < stock.getHistory().size(); i++) {
                HistoricalQuote historicalQuote = stock.getHistory().get(i);
                Calendar hqDate = historicalQuote.getDate();
                Calendar dateTo = (Calendar) hqDate.clone();
                dateTo.add(Calendar.DATE, -1);

                if (!dateTo.before(from)) {
                    List<HistoricalQuote> quotes =
                            stock.getHistory().subList(i - 200, i);

                    if (quotes.size() > 1)
                        items.add(new TwoHundredDaysIndicators(historicalQuote,
                                Calculator.average(QuoteUtil.getHistoryAdjCloses(quotes)),
                                Calculator.stdev(QuoteUtil.getHistoryAdjCloses(quotes), Variables.TH_MA_STDEV_TYPE)));
                }
            }
            Collections.sort(items);
            items.stream().forEach((indicator) -> log.debug("Indicator: {}", indicator));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return items;
    }

    @Override
    public List<TwoHundredDaysIndicators> getTwoHundredDaysIndicators(String ticker, Calendar from, Calendar to) {
        Calendar fetchFrom = (Calendar) from.clone();
        fetchFrom.add(Calendar.DATE, -Variables.TWO_HUNDRED_DAYS);

        Stock stock;
        List<TwoHundredDaysIndicators> items = new ArrayList<>();
        try {
            stock = YahooFinance.get(ticker, fetchFrom, to, Interval.DAILY);

            _addTodayHistoricalQuote(stock, to);

            for (int i = 0; i < stock.getHistory().size(); i++) {
                HistoricalQuote historicalQuote = stock.getHistory().get(i);
                Calendar hqDate = historicalQuote.getDate();
                Calendar dateTo = (Calendar) hqDate.clone();
                dateTo.add(Calendar.DATE, -1);
                Calendar dateFrom = (Calendar) hqDate.clone();
                dateFrom.add(Calendar.DATE, -Variables.TWO_HUNDRED_DAYS);

                if (!dateTo.before(from)) {
                    List<HistoricalQuote> quotes =
                            stock.getHistory().stream()
                                    .filter(hq -> !hq.getDate().before(dateFrom) && !hq.getDate().after(dateTo))
                                    .collect(Collectors.toList());

                    log.debug("Dates: {} ~ {}", dateFrom.getTime(), dateTo.getTime());
                    log.debug("History: {}", QuoteUtil.getHistoryAdjCloses(quotes));

                    if (quotes.size() > 1)
                        items.add(new TwoHundredDaysIndicators(historicalQuote,
                                Calculator.average(QuoteUtil.getHistoryAdjCloses(quotes)),
                                Calculator.stdev(QuoteUtil.getHistoryAdjCloses(quotes), Variables.TH_MA_STDEV_TYPE)));
                }
            }
            Collections.sort(items);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return items;
    }

    @Override
    public List<BollingerBandIndicators> getBollingerBand(String ticker, Calendar from, Calendar to, int days, BigDecimal multiplier) {
        Assert.isTrue(days <= Variables.BB_MAX_DAYS,
                String.format("Please supply less than %s days", Variables.BB_MAX_DAYS));
        Calendar fetchFrom = (Calendar) from.clone();
        fetchFrom.add(Calendar.DATE, -days * 2);

        Stock stock;
        List<BollingerBandIndicators> items = new ArrayList<>();
        try {
            stock = YahooFinance.get(ticker, fetchFrom, to, Interval.DAILY);

            _addTodayHistoricalQuote(stock, to);

            for (int i = 0; i < stock.getHistory().size(); i++) {
                HistoricalQuote historicalQuote = stock.getHistory().get(i);
                Calendar hqDate = historicalQuote.getDate();
                Calendar dateTo = (Calendar) hqDate.clone();
                dateTo.add(Calendar.DATE, -1);

                if (!dateTo.before(from)) {
                    List<HistoricalQuote> quotes =
                            stock.getHistory().subList(i + 1 - days, i + 1);

                    if (quotes.size() > 1) {
                        items.add(new BollingerBandIndicators(historicalQuote,
                                Calculator.average(QuoteUtil.getHistoryAdjCloses(quotes)),
                                Calculator.stdev(QuoteUtil.getHistoryAdjCloses(quotes), Variables.BB_STDEV_TYPE), multiplier));
                    }
                }
            }
            Collections.sort(items);
            items.forEach(item -> log.debug(item.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return items;
    }

    @Override
    public void getFriday() {
        try {
            Stock stock = YahooFinance.get("VTI", Interval.WEEKLY);
            log.debug("Stock weekly {}", stock);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void _addTodayHistoricalQuote(Stock stock, Calendar to) throws IOException {
        if (!to.before(DateUtil.midnight())) {
            // Adding today when there is no HistoricalQuote for today (at trading hours)
            Calendar midnight = DateUtil.midnight();

            List<HistoricalQuote> filtered = stock.getHistory().stream()
                    .filter(historicalQuote -> historicalQuote.getDate().equals(midnight))
                    .collect(Collectors.toList());

            if (filtered.isEmpty()) {
                HistoricalQuote present = new HistoricalQuote();
                present.setDate(midnight);
                present.setClose(stock.getQuote().getPrice());
                present.setAdjClose(stock.getQuote().getPrice());
                stock.getHistory().add(present);
            }
        }
    }

}

