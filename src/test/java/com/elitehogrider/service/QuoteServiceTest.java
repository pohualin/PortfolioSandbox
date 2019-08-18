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

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class QuoteServiceTest {

    private final Logger log = LoggerFactory.getLogger(QuoteServiceTest.class);

    @Autowired
    QuoteService quoteService;

    @Test
    public void quoteMatrix() throws IOException {
        log.debug("T TwoHundredDaysIndicators: {}", quoteService.getTwoHundredDaysIndicators("T").toString());
    }

}