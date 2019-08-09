package com.elitehogrider.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class QuoteUtilTest {

    private final Logger log = LoggerFactory.getLogger(QuoteUtilTest.class);

    @Test
    public void getHistoryCloses() throws IOException {
        Calendar from = Calendar.getInstance();
        from.add(Calendar.DATE, -200);
        List<BigDecimal> histories =
                QuoteUtil.getHistoryCloses(YahooFinance.get("T", from, Interval.DAILY).getHistory());
        log.debug("{} History Closes: {}", histories.size(), histories);
    }

}