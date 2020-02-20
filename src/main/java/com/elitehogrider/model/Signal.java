package com.elitehogrider.model;

import com.elitehogrider.model.indicator.Indicators;
import org.springframework.core.style.ToStringCreator;

import java.util.Calendar;

public class Signal {
    Calendar date;
    Ticker ticker;
    TradeType type;
    Indicators indicators;
    SignalStatus status;

    public Signal(Calendar date, Ticker ticker, TradeType type, Indicators indicators, SignalStatus status) {
        this.date = date;
        this.ticker = ticker;
        this.type = type;
        this.indicators = indicators;
        this.status = status;
    }

    public Calendar getDate() {
        return date;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public TradeType getType() {
        return type;
    }

    public Indicators getIndicators() {
        return indicators;
    }

    public SignalStatus getStatus() {
        return status;
    }

    public void setStatus(SignalStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("Date", date.getTime())
                .append("Ticker", ticker)
                .append("Type", type)
                .append("Status", status)
                .toString();
    }
}
