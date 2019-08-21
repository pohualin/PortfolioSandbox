package com.elitehogrider.model;

import java.util.UUID;

public class Trader {

    Long id;
    String name;
    Account account;

    public Trader(String name) {
        this.id = UUID.randomUUID().getMostSignificantBits();
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
