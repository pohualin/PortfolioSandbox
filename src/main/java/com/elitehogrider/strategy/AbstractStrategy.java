package com.elitehogrider.strategy;

import com.elitehogrider.model.Order;
import com.elitehogrider.service.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

public class AbstractStrategy implements Strategy {

    private static final Logger log = LoggerFactory.getLogger(AbstractStrategy.class);

    @Autowired
    TradeService tradeService;

    @Override
    public List<Order> identifySignal() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Order> generateOrders(Long traderId) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void execute(Long traderId) {
        List<Order> orders = generateOrders(traderId);
        log.debug("{} orders to be executed", orders.size());
        orders.stream().forEach((order -> {
            switch (order.getType()) {
                case BUY:
                    tradeService.buy(traderId, order);
                    break;
                case SELL:
                    tradeService.sell(traderId, order);
                    break;
            }
        }));
    }
}
