package com.elitehogrider.api;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import yahoofinance.Stock;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class QuoteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    QuoteController quoteContoller;

    @Test
    public void getStock() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/quote/getStock/T").accept(MediaType.APPLICATION_JSON).content("T"))
                .andExpect(status().isOk())
                .andReturn();

        Stock stock = quoteContoller.getStock("T");
        Assert.assertTrue(stock.getSymbol().equalsIgnoreCase("T"));
    }

}