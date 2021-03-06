package com.elitehogrider.api;

import com.elitehogrider.model.Ticker;
import com.elitehogrider.util.QuoteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("quote")
public class QuoteController {

    private static Logger log = LoggerFactory.getLogger(QuoteController.class);

    @RequestMapping("getStock/{ticker}")
    public Stock getStock(@PathVariable String ticker) throws IOException {
        return YahooFinance.get(ticker, true);
    }

    @RequestMapping("getAllTracking")
    public Map<String, Stock> getAllTracking() throws IOException {
        String[] tickers = Arrays.stream(Ticker.values()).map(ticker -> ticker.name()).toArray(String[]::new);
        return YahooFinance.get(tickers);
    }

    @RequestMapping("getHistory/{ticker}")
    public Stock getHistory(@PathVariable String ticker) throws IOException {
        Calendar ago = Calendar.getInstance();
        ago.add(Calendar.YEAR, -1);
        return YahooFinance.get(ticker, ago, Interval.DAILY);
    }

    @RequestMapping("getHistoryCloses/{ticker}")
    public List<BigDecimal> getHistoryCloses(@PathVariable String ticker) throws IOException {
        Calendar ago = Calendar.getInstance();
        ago.add(Calendar.YEAR, -1);
        return QuoteUtil.getHistoryCloses(YahooFinance.get(ticker, ago, Interval.DAILY).getHistory());
    }

}