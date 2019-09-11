package com.elitehogrider.validator;

import com.elitehogrider.model.Account;
import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Ticker;

import java.util.HashSet;
import java.util.Set;

public class AccountValidator {

    /**
     * Holdings should not contains any stocks removed from portfolio
     */
    public static void validate(Account account, Portfolio portfolio) {
        Set<Ticker> uniqueTickers = new HashSet<>();
        account.getHoldings().forEach((holding -> uniqueTickers.add(holding.getTicker())));

        uniqueTickers.forEach(ticker -> {
            if (!portfolio.getAllocation().keySet().contains(ticker)) {
                throw new IllegalArgumentException(String.format(ValidationMessages.PLEASE_SELL_HOLDINGS, ticker.name()));
            }
        });
    }
}
