package com.elitehogrider.strategy;

import com.elitehogrider.model.Order;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TraderAStrategy extends AbstractStrategy implements Strategy {
    @Override
    public List<Order> identifySignal() {
        return Collections.emptyList();
    }

    @Override
    public List<Order> generateOrders(Long traderId) {
        return identifySignal();
    }
}
