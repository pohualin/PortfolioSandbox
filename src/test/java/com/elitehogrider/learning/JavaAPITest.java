package com.elitehogrider.learning;

import com.elitehogrider.util.DateUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.tomcat.jni.Local;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class JavaAPITest {

    private final Logger log = LoggerFactory.getLogger(JavaAPITest.class);

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void subList() {
        List<String> stringList = new ArrayList<>();
        stringList.add("A");
        stringList.add("B");
        stringList.add("C");
        stringList.add("D");
        stringList.add("E");

        log.debug(Arrays.toString(stringList.subList(0, 1).toArray()));
        log.debug(Arrays.toString(stringList.subList(0, 2).toArray()));
        log.debug(Arrays.toString(stringList.subList(0, 4).toArray()));
        log.debug(Arrays.toString(stringList.subList(0, 5).toArray()));

        log.debug(Arrays.toString(stringList.subList(4 - 3, 4).toArray()));
        log.debug(Arrays.toString(stringList.subList(5 - 3, 5).toArray()));

    }

    @Test
    public void predicatedMap() {
        Map<String, BigDecimal> map = new HashMap<>();
        Map<String, BigDecimal> predicatedMap = MapUtils.predicatedMap(map, null, v -> map.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add).add(v).setScale(2, BigDecimal.ROUND_HALF_UP)
                .compareTo(new BigDecimal(100.00)) < 1);

        List<BigDecimal> bds = new ArrayList<>();
        bds.add(new BigDecimal(10.50));
        bds.add(new BigDecimal(10.49));
        bds.add(new BigDecimal(79.01));
        BigDecimal total = bds.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);
        log.debug("total: {}", total);

        predicatedMap.putIfAbsent("A", new BigDecimal(10.50));
        predicatedMap.putIfAbsent("B", new BigDecimal(10.49));
        predicatedMap.putIfAbsent("C", new BigDecimal(79.01));

        log.debug("Map keys: {} values: {}", map.keySet(), map.values());
        log.debug("PredicatedMap keys: {} values: {}", predicatedMap.keySet(), predicatedMap.values());
    }

    @Test
    public void unmodifiableMap() {
        Map<String, String> map = new HashMap<>();
        map.put("A", "A");
        map = Collections.unmodifiableMap(map);

        expectedEx.expect(UnsupportedOperationException.class);
        map.put("B", "B");
    }

    @Test
    public void localDate() {
        LocalDate now = LocalDate.now();
        LocalDate currentMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate previousMonday = currentMonday.minus(1, ChronoUnit.WEEKS);
        log.debug("Now: {}", now);
        log.debug("Monday: {}", currentMonday);
        log.debug("Previous Monday: {}", previousMonday);

        Calendar nowCal = DateUtil.localDateToCalendar(now);
        log.debug("NowCal: {}", nowCal);

        Calendar today = DateUtil.midnight();
        Assert.assertEquals(nowCal, today);
    }

    @Test
    public void bigDecimalTest() {
        double d = 299.50000;
        BigDecimal zero_rhp = new BigDecimal(d).setScale(0, BigDecimal.ROUND_HALF_UP);
        BigDecimal two_rhp = new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP);
        log.debug("Zero < Two? {}", zero_rhp.compareTo(two_rhp) < 0 );
        log.debug("Zero == Two? {}", zero_rhp.compareTo(two_rhp) == 0 );
        log.debug("Zero > Two? {}", zero_rhp.compareTo(two_rhp) > 0 );
        Assert.assertTrue(zero_rhp.compareTo(two_rhp) > 0);
    }
}