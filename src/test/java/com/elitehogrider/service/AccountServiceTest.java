package com.elitehogrider.service;

import com.elitehogrider.model.Account;
import com.elitehogrider.model.Holding;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Ticker;
import com.elitehogrider.validator.ValidationMessages;
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

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class AccountServiceTest {

    private final Logger log = LoggerFactory.getLogger(AccountServiceTest.class);

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void newAccount() {
        HashMap<Ticker, BigDecimal> allocation = new HashMap<>();
        allocation.putIfAbsent(Ticker.VTI, new BigDecimal(60));
        allocation.putIfAbsent(Ticker.BND, new BigDecimal(40));
        Portfolio portfolio = new Portfolio(allocation);
        Account account = new Account(new BigDecimal(10000), portfolio);
    }

    @Test
    public void unmodifiablePortfolio() {
        HashMap<Ticker, BigDecimal> allocation = new HashMap<>();
        allocation.putIfAbsent(Ticker.VTI, new BigDecimal(60));
        allocation.putIfAbsent(Ticker.BND, new BigDecimal(40));
        Portfolio portfolio = new Portfolio(allocation);
        Account account = new Account(new BigDecimal(10000), portfolio);

        expectedEx.expect(UnsupportedOperationException.class);
        account.getPortfolio().getAllocation().putIfAbsent(Ticker.T, new BigDecimal(100));
    }

    @Test
    public void updatePortfolioWithHoldings() {
        HashMap<Ticker, BigDecimal> allocation = new HashMap<>();
        allocation.putIfAbsent(Ticker.VTI, new BigDecimal(60));
        allocation.putIfAbsent(Ticker.BND, new BigDecimal(40));
        Portfolio portfolio = new Portfolio(allocation);
        Account account = new Account(new BigDecimal(10000), portfolio);
        account.getHoldings().add(new Holding(Ticker.T, new BigDecimal(20), new BigDecimal(10)));

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage(String.format(ValidationMessages.PLEASE_SELL_HOLDINGS, Ticker.T.name()));

        allocation.clear();
        allocation.putIfAbsent(Ticker.VTI, new BigDecimal(100));
        account.setPortfolio(new Portfolio(allocation));
    }

    @Test
    public void badPortfolioOver100Allocation() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage(ValidationMessages.INVALID_PORTFOLIO);

        HashMap<Ticker, BigDecimal> allocation = new HashMap<>();
        allocation.putIfAbsent(Ticker.VTI, new BigDecimal(60));
        allocation.putIfAbsent(Ticker.BND, new BigDecimal(41));
        Portfolio portfolio = new Portfolio(allocation);
    }

    @Test
    public void badPortfolioLessThan100Allocation() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage(ValidationMessages.INVALID_PORTFOLIO);

        HashMap<Ticker, BigDecimal> allocation = new HashMap<>();
        allocation.putIfAbsent(Ticker.VTI, new BigDecimal(60));
        allocation.putIfAbsent(Ticker.BND, new BigDecimal(39.99));
        Portfolio portfolio = new Portfolio(allocation);
    }

    @Test
    public void badPortfolioNegativeAllocation() {
        // Negative allocation
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage(ValidationMessages.INVALID_PORTFOLIO);

        HashMap<Ticker, BigDecimal> allocation = new HashMap<>();
        allocation.putIfAbsent(Ticker.VTI, new BigDecimal(-1));
        allocation.putIfAbsent(Ticker.BND, new BigDecimal(40));
        Portfolio portfolio = new Portfolio(allocation);
    }

    @Test
    public void badPortfolioZeroAllocation() {
        // Negative allocation
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage(ValidationMessages.INVALID_PORTFOLIO);

        HashMap<Ticker, BigDecimal> allocation = new HashMap<>();
        allocation.putIfAbsent(Ticker.VTI, new BigDecimal(0));
        allocation.putIfAbsent(Ticker.BND, new BigDecimal(40));
        Portfolio portfolio = new Portfolio(allocation);
    }

}