package com.elitehogrider.model;

import java.util.List;

public class SimulateResult {
    List<Signal> signals;

    public SimulateResult() {
    }

    public void setSignals(List<Signal> signals) {
        this.signals = signals;
    }

    public List<Signal> getSignals() {
        return signals;
    }
}