package com.elitehogrider.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuoteController {

    @RequestMapping("/getStock")
    public String getStock() {
        return "Greetings from Spring Boot!";
    }

}