package com.elitehogrider.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/calculate")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}