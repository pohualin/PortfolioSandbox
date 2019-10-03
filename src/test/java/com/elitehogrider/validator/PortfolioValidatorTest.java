package com.elitehogrider.validator;

import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Ticker;
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
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class PortfolioValidatorTest {

    private final Logger log = LoggerFactory.getLogger(PortfolioValidatorTest.class);

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void invalidAllocation() {
        // null allocation
        expectedEx.expect(IllegalArgumentException.class);
        PortfolioValidator.validate(null);

        // contains zero
        expectedEx.expect(IllegalArgumentException.class);
        Map<Ticker, BigDecimal> allocation = new HashMap<>();
        allocation.putIfAbsent(Ticker.T, BigDecimal.ZERO);
        allocation.putIfAbsent(Ticker.VZ, new BigDecimal(100));
        Portfolio portfolio = new Portfolio(allocation);

        // contains negative
        expectedEx.expect(IllegalArgumentException.class);
        allocation.clear();
        allocation.putIfAbsent(Ticker.T, new BigDecimal(-1));
        allocation.putIfAbsent(Ticker.VZ, new BigDecimal(101));
        portfolio = new Portfolio(allocation);

        // total not 100
        expectedEx.expect(IllegalArgumentException.class);
        allocation.clear();
        allocation.putIfAbsent(Ticker.T, new BigDecimal(2));
        allocation.putIfAbsent(Ticker.VZ, new BigDecimal(99));
        portfolio = new Portfolio(allocation);

    }

    @Test
    public void validPortfolio() {
        Map<Ticker, BigDecimal> allocation = new HashMap<>();
        allocation.putIfAbsent(Ticker.T, new BigDecimal(1));
        allocation.putIfAbsent(Ticker.VZ, new BigDecimal(99));
    }

}