package com.elitehogrider.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yahoofinance.Stock;
import yahoofinance.histquotes.HistoricalQuote;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public final class QuoteUtil {

    private static final Logger log = LoggerFactory.getLogger(QuoteUtil.class);

    private QuoteUtil() {
    }

    public static List<BigDecimal> getHistoryCloses(List<HistoricalQuote> historicalQuotes) throws IOException {
        return historicalQuotes.stream()
                .map(historicalQuote -> historicalQuote.getClose().setScale(2, BigDecimal.ROUND_HALF_UP))
                .collect(Collectors.toList());
    }

    public static List<BigDecimal> getHistoryAdjCloses(List<HistoricalQuote> historicalQuotes) throws IOException {
        return historicalQuotes.stream()
                .map(historicalQuote -> historicalQuote.getAdjClose().setScale(2, BigDecimal.ROUND_HALF_UP))
                .collect(Collectors.toList());
    }
}
