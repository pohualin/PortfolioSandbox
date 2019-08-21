package com.elitehogrider.validator;

import com.elitehogrider.model.Account;
import com.elitehogrider.model.Holding;
import com.elitehogrider.model.Order;

import java.math.BigDecimal;
import java.util.List;

public class OrderValidator {

    public static boolean isValid(Account account, Order order) {
        boolean valid = false;
        switch (order.getType()) {
            case BUY:
                BigDecimal amount = order.getPrice().multiply(order.getShares());
                valid = account.getCash().compareTo(amount) != -1;
                break;
            case SELL:
                List<Holding> holding = account.getHoldingByTicker(order.getTicker());
                if (!holding.isEmpty()) {
                    valid = holding.get(0).getShares().compareTo(order.getShares()) != -1;
                }
                break;
        }
        return valid;
    }
}
