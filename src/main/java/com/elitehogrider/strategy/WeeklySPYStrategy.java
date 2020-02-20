package com.elitehogrider.strategy;

import com.elitehogrider.model.indicator.GenericIndicators;
import com.elitehogrider.model.indicator.IndicatorType;
import com.elitehogrider.model.param.DayOfWeekParam;
import com.elitehogrider.model.param.MovingAverageParam;
import com.elitehogrider.service.QuoteService;
import com.elitehogrider.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class WeeklySPYStrategy {

    private final Logger log = LoggerFactory.getLogger(WeeklySPYStrategy.class);

    @Autowired
    QuoteService _quoteService;

    private static DayOfWeek dayOfWeek = DayOfWeek.WEDNESDAY;
    private static int calendarDayOfWeek = Calendar.WEDNESDAY;
    private static int calendarDayOfWeekFuture = Calendar.WEDNESDAY;
    private static int offset = calendarDayOfWeekFuture - calendarDayOfWeek;
    private static int xYears = 25;
    private static int xWeeks = 1;

    public void simulate() {
        LocalDate today = LocalDate.now().with(TemporalAdjusters.previous(dayOfWeek));
        LocalDate yearsBack = today.minusYears(xYears);
        LocalDate simTo = today.minusWeeks(xWeeks);
        LocalDate simFrom = yearsBack.minusWeeks(xWeeks);

        Calendar from = DateUtil.localDateToCalendar(simFrom);
        Calendar to = DateUtil.localDateToCalendar(simTo);

        // Current
        DayOfWeekParam dayOfWeekParam = new DayOfWeekParam("SPY", calendarDayOfWeek, Interval.DAILY, from, to);
        List<HistoricalQuote> days = _quoteService.getDayOfWeek(dayOfWeekParam);

        // Future
        Calendar futureTo = DateUtil.localDateToCalendar(today);
        Calendar futureFrom = DateUtil.localDateToCalendar(yearsBack);

        DayOfWeekParam futureDayOfWeekParam = new DayOfWeekParam("SPY", calendarDayOfWeekFuture, Interval.DAILY, futureFrom, futureTo);
        List<HistoricalQuote> futureDays = _quoteService.getDayOfWeek(futureDayOfWeekParam);

        // Data
        Map<Calendar, GenericIndicators> data = new TreeMap<>();
        days.forEach(day -> {
            GenericIndicators gi = new GenericIndicators(day);
            HistoricalQuote future = _findNext(day.getDate(), xWeeks, futureDays);
            if (future != null) {
                gi.getIndicatorMap().putIfAbsent(IndicatorType.FUTURE_DATE, future.getDate());
                gi.getIndicatorMap().putIfAbsent(IndicatorType.FUTURE_WEEK_CLOSE, future.getClose().setScale(2, BigDecimal.ROUND_HALF_UP));
                data.putIfAbsent(day.getDate(), gi);
            }
        });

        // Indicators
        MovingAverageParam _200MAParam = new MovingAverageParam("SPY", 200, Interval.DAILY, from, to);
        Map<Calendar, BigDecimal> _200MA = _quoteService.getMovingAverage(_200MAParam);
        data.entrySet().forEach(entry ->
                entry.getValue().getIndicatorMap().putIfAbsent(IndicatorType.MA_200_SIMPLE, _200MA.get(entry.getKey()))
        );

        MovingAverageParam _10MAParam = new MovingAverageParam("SPY", 10, Interval.DAILY, from, to);
        Map<Calendar, BigDecimal> _10MA = _quoteService.getMovingAverage(_10MAParam);
        data.entrySet().forEach(entry ->
                entry.getValue().getIndicatorMap().putIfAbsent(IndicatorType.MA_10_SIMPLE, _10MA.get(entry.getKey()))
        );

        MovingAverageParam _50MAParam = new MovingAverageParam("SPY", 50, Interval.DAILY, from, to);
        Map<Calendar, BigDecimal> _50MA = _quoteService.getMovingAverage(_50MAParam);
        data.entrySet().forEach(entry ->
                entry.getValue().getIndicatorMap().putIfAbsent(IndicatorType.MA_50_SIMPLE, _50MA.get(entry.getKey()))
        );

        int total = data.entrySet().size();
        int trade = 0;
        int win = 0;
        int loss = 0;
        int maxLoss = 0;

        for (Map.Entry<Calendar, GenericIndicators> entry : data.entrySet()) {
            GenericIndicators indicators = entry.getValue();

            BigDecimal base = indicators.getHistoricalQuote().getLow().setScale(0, BigDecimal.ROUND_DOWN);

            // indicators.getHistoricalQuote().getLow().setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(indicators.getIndicatorMap().get(IndicatorType.MA_200_SIMPLE)) > 0
            // indicators.getIndicatorMap().get(IndicatorType.MA_10_SIMPLE).setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(indicators.getIndicatorMap().get(IndicatorType.MA_50_SIMPLE)) > 0
            if (true) {
                trade++;
                if (((BigDecimal) indicators.getIndicatorMap().get(IndicatorType.FUTURE_WEEK_CLOSE)).compareTo(base) > 0) {
                    win++;
                    log.debug("Win: {} low {} {} close {}",
                            indicators.getHistoricalQuote().getDate().getTime(),
                            base,
                            ((Calendar) indicators.getIndicatorMap().get(IndicatorType.FUTURE_DATE)).getTime(),
                            indicators.getIndicatorMap().get(IndicatorType.FUTURE_WEEK_CLOSE));
                } else {
                    loss++;
                    BigDecimal baseMinusOne = new BigDecimal(base.toString()).subtract(new BigDecimal(1));

                    if (baseMinusOne.compareTo((BigDecimal) indicators.getIndicatorMap().get(IndicatorType.FUTURE_WEEK_CLOSE)) > 0) {
                        maxLoss++;
                    }

                    log.debug("Loss: {} low {} lower {} {} close {}",
                            indicators.getHistoricalQuote().getDate().getTime(),
                            base,
                            baseMinusOne,
                            ((Calendar) indicators.getIndicatorMap().get(IndicatorType.FUTURE_DATE)).getTime(),
                            indicators.getIndicatorMap().get(IndicatorType.FUTURE_WEEK_CLOSE));
                }
            }
        }

        log.debug("Total days: {} Trade: {} Won: {} Loss: {} Max Loss: {}",
                total, trade, win, loss, maxLoss);

        log.debug("Total days: {} Trade rate: {}% Won Rate: {}% Loss Rate: {}% Max Loss Rate: {}%",
                total,
                BigDecimal.valueOf((double) trade * 100 / total).setScale(2, BigDecimal.ROUND_HALF_UP),
                BigDecimal.valueOf((double) win * 100 / trade).setScale(2, BigDecimal.ROUND_HALF_UP),
                BigDecimal.valueOf((double) loss * 100 / trade).setScale(2, BigDecimal.ROUND_HALF_UP),
                BigDecimal.valueOf((double) maxLoss * 100 / trade).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    private HistoricalQuote _findNext(Calendar date, int weeks, List<HistoricalQuote> futures) {
        Calendar today = DateUtil.midnight();
        Calendar target = (Calendar) date.clone();
        target.add(Calendar.DATE, 7 * weeks);
        target.add(Calendar.DATE, offset);
        Map<Calendar, HistoricalQuote> map = new HashMap<>();
        futures.forEach(future -> map.putIfAbsent(future.getDate(), future));
        if (map.get(target) == null && target.compareTo(today) < 0) {
            return _findNext(target, 1, futures);
        } else {
            return map.get(target);
        }
    }

}
