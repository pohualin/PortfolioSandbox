package com.elitehogrider.strategy;

import com.elitehogrider.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class TraderATest extends AbstractStrategyTest {

    private final Logger log = LoggerFactory.getLogger(TraderATest.class);

    @Autowired
    TraderAStrategy traderAStrategy;

    @Test
    public void identifySignal() {
        traderAStrategy.identifySignal(trader.getAccount().getPortfolio());
    }

    @Test
    public void simulate() {
        Calendar from = DateUtil.parseDateString("2018-01-01");
        Calendar to = DateUtil.parseDateString("2019-10-01");
        traderAStrategy.simulate(trader.getId(), from, to);
    }

}