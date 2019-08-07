package com.elitehogrider.api;

import com.elitehogrider.model.Ticker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("quote")
public class QuoteController {

    private static Logger log = LoggerFactory.getLogger(QuoteController.class);

    @RequestMapping("/getStock/{ticker}")
    public Stock getStock(@PathVariable String ticker) throws IOException {
        return YahooFinance.get(ticker, true);
    }

    @RequestMapping("getAllTracking")
    public Map<String, Stock> getAllTracking() throws IOException {
        String[] tickers = Arrays.stream(Ticker.values()).map(ticker -> ticker.name()).toArray(String[]::new);
        return YahooFinance.get(tickers);
    }

}