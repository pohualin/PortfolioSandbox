package com.elitehogrider.strategy;

import com.elitehogrider.model.Portfolio;
import com.elitehogrider.model.Signal;
import com.elitehogrider.service.QuoteService;
import com.elitehogrider.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * RothIRAStratagy
 * <p>
 * Contains VTI, VNQ, BND and FLRN
 * <p>
 * Add 100 dollars every Monday
 * <p>
 * VTI percentage = 60 - previous Friday close
 * VNQ set to 8%
 * Split 50/50 the rest to BNQ and FLRN
 */
@Service
public class RothIRAStratagy extends AbstractStrategy implements Strategy {

    @Autowired
    QuoteService quoteService;

    @Override
    public List<Signal> identifySignal(Portfolio portfolio) {
        Calendar from = DateUtil.midnight();
        from.add(Calendar.DATE, -1);
        Calendar today = Calendar.getInstance();
        return this.identifySignal(portfolio, from, today);
    }

    @Override
    public List<Signal> identifySignal(Portfolio portfolio, Calendar from, Calendar to) {
        if (portfolio.getAllocation().isEmpty()) {
            throw new RuntimeException("Account contains no stocks");
        }
        List<Signal> signals = new ArrayList<>();
        return signals;
    }

}
