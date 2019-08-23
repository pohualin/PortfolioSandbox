package com.elitehogrider.model;

import java.util.List;

public class SimulateResult {

    String summary;
    Account account;
    List<Signal> signals;

    public SimulateResult() {
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setSignals(List<Signal> signals) {
        this.signals = signals;
    }

    public List<Signal> getSignals() {
        return signals;
    }

}