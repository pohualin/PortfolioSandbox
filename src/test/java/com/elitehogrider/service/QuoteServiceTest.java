package com.elitehogrider.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Calendar;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class QuoteServiceTest {

    private final Logger log = LoggerFactory.getLogger(QuoteServiceTest.class);

    @Autowired
    QuoteService quoteService;

    @Test
    public void twoHundredDaysIndicators() throws IOException {
        Calendar today = Calendar.getInstance();
        Calendar from = (Calendar) today.clone();
        from.add(Calendar.YEAR, -1);
        Calendar to = (Calendar) today.clone();
        to.add(Calendar.MONTH, -1);
        quoteService.getTwoHundredDaysIndicators("T", from, to);
    }

    @Test
    public void twoHundredDaysIndicatorsBadDates() throws IOException {
        Calendar today = Calendar.getInstance();
        Calendar from = (Calendar) today.clone();
        from.add(Calendar.YEAR, -1);
        Calendar to = (Calendar) today.clone();
        to.add(Calendar.YEAR, -2);
        quoteService.getTwoHundredDaysIndicators("T", from, to);
    }

}