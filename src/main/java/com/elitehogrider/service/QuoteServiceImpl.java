package com.elitehogrider.service;

import com.elitehogrider.config.Variables;
import com.elitehogrider.model.indicator.BollingerBandIndicators;
import com.elitehogrider.model.indicator.TwoHundredDaysIndicators;
import com.elitehogrider.model.param.DayOfWeekParam;
import com.elitehogrider.model.param.MovingAverageParam;
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
import java.util.Map;
import java.util.TreeMap;
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
    public List<HistoricalQuote> getDayOfWeek(DayOfWeekParam params) {
        Stock stock;
        List<HistoricalQuote> quotes;
        try {
            stock = YahooFinance.get(params.getTicker(), params.getFrom(), params.getTo(), params.getInterval());
            quotes = stock.getHistory().stream()
                    .filter((hq -> hq.getDate().get(Calendar.DAY_OF_WEEK) == params.getDayOfWeek()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return quotes;
    }

    @Override
    public Map<Calendar, BigDecimal> getMovingAverage(MovingAverageParam params) {
        Calendar fetchFrom = (Calendar) params.getFrom().clone();
        fetchFrom.add(Calendar.DATE, -params.getPeriod() * 2);

        Stock stock;
        Map<Calendar, BigDecimal> items = new TreeMap<>();
        try {
            stock = YahooFinance.get(params.getTicker(), fetchFrom, params.getTo(), params.getInterval());

            _addTodayHistoricalQuote(stock, params.getTo());

            for (int i = 0; i < stock.getHistory().size(); i++) {
                HistoricalQuote historicalQuote = stock.getHistory().get(i);
                Calendar hqDate = historicalQuote.getDate();
                Calendar dateTo = (Calendar) hqDate.clone();
                // dateTo.add(Calendar.DATE, -1);

                if (!dateTo.before(params.getFrom())) {
                    List<HistoricalQuote> quotes =
                            stock.getHistory().subList(i - params.getPeriod() + 1, i + 1);
                    if (quotes.size() > 1) {
                        items.putIfAbsent(historicalQuote.getDate(),
                                Calculator.average(QuoteUtil.getHistoryCloses(quotes)));
                    }
                }
            }
            items.entrySet().forEach((entry) -> log.info("Date: {}, {} SMA: {}", entry.getKey().getTime(), params.getPeriod(), entry.getValue()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    private void _addTodayHistoricalQuote(Stock stock, Calendar to) throws IOException {
        if (!to.before(DateUtil.midnight())) {
            // Adding today when there is no HistoricalQuote for today (during trading hours)
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

