package com.elitehogrider.service;

import com.elitehogrider.model.param.DayOfWeekParam;
import com.elitehogrider.model.param.MovingAverageParam;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class QuoteServiceTest {

    private final Logger log = LoggerFactory.getLogger(QuoteServiceTest.class);

    @Autowired
    QuoteService quoteService;

    @Test
    public void twoHundred() {
        Calendar today = Calendar.getInstance();
        Calendar from = (Calendar) today.clone();
        from.add(Calendar.YEAR, -1);
        Calendar to = (Calendar) today.clone();
        // to.add(Calendar.MONTH, -1);
        quoteService.getTwoHundred("T", from, to);
    }

    @Test
    public void twoHundredDaysIndicators() {
        Calendar today = Calendar.getInstance();
        Calendar from = (Calendar) today.clone();
        from.add(Calendar.YEAR, -1);
        Calendar to = (Calendar) today.clone();
        to.add(Calendar.MONTH, -1);
        quoteService.getTwoHundredDaysIndicators("T", from, to);
    }

    @Test
    public void twoHundredDaysIndicatorsBadDates() {
        Calendar today = Calendar.getInstance();
        Calendar from = (Calendar) today.clone();
        from.add(Calendar.YEAR, -1);
        Calendar to = (Calendar) today.clone();
        to.add(Calendar.YEAR, -2);
        quoteService.getTwoHundredDaysIndicators("T", from, to);
    }

    @Test
    public void getBollingerBand() {
        Calendar today = Calendar.getInstance();
        Calendar from = (Calendar) today.clone();
        from.add(Calendar.YEAR, -1);
        Calendar to = (Calendar) today.clone();
        quoteService.getBollingerBand("T", from, to, 20, new BigDecimal(2));
    }

    @Test
    public void getFriday() throws IOException {
        DayOfWeekParam fridayParam = new DayOfWeekParam("SPY", Calendar.FRIDAY);
        List<HistoricalQuote> fridays = quoteService.getDayOfWeek(fridayParam);
        fridays.forEach((friday) -> {
            log.debug(friday.getDate().getTime() + ": " + friday.getClose().setScale(2, BigDecimal.ROUND_HALF_UP));
        });
    }

    @Test
    public void getMovingAverage() {

        Calendar today = new GregorianCalendar(2020, 0, 1);
        Calendar from = (Calendar) today.clone();
        from.add(Calendar.MONTH, -1);

        // 10 days SMA
        MovingAverageParam params = new MovingAverageParam("SPY", 10, Interval.DAILY, from, today);
        Map<Calendar, BigDecimal> _10MA = quoteService.getMovingAverage(params);

        List<Calendar> dates = new ArrayList<>(_10MA.keySet());
        Calendar _12312019 = new GregorianCalendar(2019, 11, 31);

        Assert.assertTrue(dates.get(dates.size() - 1).equals(_12312019));
        Assert.assertEquals(Arrays.asList(_10MA.values().toArray()).get(dates.size() - 1), new BigDecimal(321.20).setScale(2, BigDecimal.ROUND_HALF_UP));

        // 50 days SMA
        MovingAverageParam _50params = new MovingAverageParam("SPY", 50, Interval.DAILY, from, today);
        Map<Calendar, BigDecimal> _50MA = quoteService.getMovingAverage(_50params);

        dates = new ArrayList<>(_50MA.keySet());

        Assert.assertTrue(dates.get(dates.size() - 1).equals(_12312019));
        Assert.assertEquals(Arrays.asList(_50MA.values().toArray()).get(dates.size() - 1), new BigDecimal(311.76).setScale(2, BigDecimal.ROUND_HALF_UP));

        // 200 days SMA
        MovingAverageParam _200params = new MovingAverageParam("SPY", 200, Interval.DAILY, from, today);
        Map<Calendar, BigDecimal> _200MA = quoteService.getMovingAverage(_200params);

        dates = new ArrayList<>(_200MA.keySet());

        Assert.assertTrue(dates.get(dates.size() - 1).equals(_12312019));
        Assert.assertEquals(Arrays.asList(_200MA.values().toArray()).get(dates.size() - 1), new BigDecimal(296.45).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

}