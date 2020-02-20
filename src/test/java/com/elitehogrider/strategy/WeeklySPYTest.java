package com.elitehogrider.strategy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class WeeklySPYTest extends AbstractStrategyTest {

    private final Logger log = LoggerFactory.getLogger(WeeklySPYTest.class);

    @Autowired
    WeeklySPYStrategy strategy;

    @Test
    public void simulate() {
        strategy.simulate();
    }

}