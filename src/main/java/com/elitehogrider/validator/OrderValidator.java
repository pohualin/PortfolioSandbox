package com.elitehogrider.validator;

import com.elitehogrider.model.Holding;
import com.elitehogrider.model.Order;
import com.elitehogrider.model.Portfolio;

import java.math.BigDecimal;
import java.util.List;

public class OrderValidator {

    public static boolean isValid(Portfolio portfolio, Order order) {
        boolean valid = false;
        switch (order.getType()) {
            case BUY:
                BigDecimal amount = order.getPrice().multiply(order.getShares());
                valid = portfolio.getCash().compareTo(amount) != -1;
                break;
            case SELL:
                if (portfolio.getHoldings().containsKey(order.getTicker())) {
                    List<Holding> holdingList = portfolio.getHoldings().get(order.getTicker());
                    if (!holdingList.isEmpty()) {
                        valid = holdingList.get(0).getShares().compareTo(order.getShares()) != -1;
                    }
                }
                break;
        }
        return valid;
    }
}
