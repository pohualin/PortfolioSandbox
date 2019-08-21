package com.elitehogrider.config;

import com.elitehogrider.model.Trader;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AllCache {

    @Bean
    public Cache<Long, Trader> traderCache() {
        return Caffeine.from("maximumSize=10,expireAfterWrite=5m").build();
    }

}
