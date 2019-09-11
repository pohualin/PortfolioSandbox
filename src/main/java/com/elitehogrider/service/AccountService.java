package com.elitehogrider.service;

import com.elitehogrider.model.Account;

import java.util.Calendar;

public interface AccountService {

    void updateValue(Account account, Calendar updatedOn);

}
