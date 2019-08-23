package com.elitehogrider.learning;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class JavaAPITest {

    private final Logger log = LoggerFactory.getLogger(JavaAPITest.class);

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testSubList() {
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

}