package com.elitehogrider.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class CalculatorTest {

    private final Logger log = LoggerFactory.getLogger(CalculatorTest.class);

    @Test
    public void twoHundredMovingAverage() throws IOException {
        Calendar today = Calendar.getInstance();

        Calendar from = (Calendar) today.clone();
        Calendar to = (Calendar) today.clone();
        from.add(Calendar.DATE, -200);
        to.add(Calendar.DATE, 0);

        Stock stock = YahooFinance.get("T", from, to, Interval.DAILY);

        BigDecimal twoHundredMA =
                Calculator.average(
                        QuoteUtil.getHistoryCloses(stock.getHistory()));
        log.debug("Calculated 200 MA {}, Fetched 200 MA {}",
                twoHundredMA, stock.getQuote().getPriceAvg200().setScale(5, BigDecimal.ROUND_HALF_UP));
        // Assert.assertTrue(twoHundredMA.equals(stock.getQuote().getPriceAvg200().setScale(5, BigDecimal.ROUND_HALF_UP)));
    }

    @Test
    public void stdev() throws IOException {
        List<BigDecimal> series = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            series.add(new BigDecimal(i));
        }

        BigDecimal mean = Calculator.average(series);
        BigDecimal stdev = Calculator.stdev(series, Calculator.StdevType.SAMPLE);

        BigDecimal[] values = new BigDecimal[5];
        values[0] = mean.subtract(stdev.multiply(new BigDecimal(2)));
        values[1] = mean.subtract(stdev);
        values[2] = mean;
        values[3] = mean.add(stdev);
        values[4] = mean.add(stdev.multiply(new BigDecimal(2)));

        log.debug("STDEV: {}", stdev);
        log.debug("Values: {}", Arrays.toString(values));

        Assert.assertTrue(mean.equals(BigDecimal.valueOf(5.5).setScale(5)));
        Assert.assertTrue(stdev.equals(BigDecimal.valueOf(3.02765)));

        Calendar today = Calendar.getInstance();

        Calendar from = (Calendar) today.clone();
        Calendar to = (Calendar) today.clone();
        from.add(Calendar.DATE, -201);
        to.add(Calendar.DATE, -1);

        BigDecimal meanT = Calculator.average(
                QuoteUtil.getHistoryCloses(
                        YahooFinance.get("T", from, to, Interval.DAILY).getHistory()));
        BigDecimal stdevT = Calculator.stdev(
                QuoteUtil.getHistoryCloses(
                        YahooFinance.get("T", from, to, Interval.DAILY).getHistory()), Calculator.StdevType.SAMPLE);

        BigDecimal[] valuesT = new BigDecimal[5];
        valuesT[0] = meanT.subtract(stdevT.multiply(new BigDecimal(2)));
        valuesT[1] = meanT.subtract(stdevT);
        valuesT[2] = meanT;
        valuesT[3] = meanT.add(stdevT);
        valuesT[4] = meanT.add(stdevT.multiply(new BigDecimal(2)));

        log.debug("Mean T: {}", meanT);
        log.debug("STDEV T: {}", stdevT);
        log.debug("Values T: {}", Arrays.toString(valuesT));
    }

}