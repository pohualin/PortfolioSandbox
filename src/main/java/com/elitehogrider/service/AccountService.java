package com.elitehogrider.service;

import com.elitehogrider.model.Account;
import com.elitehogrider.model.Ticker;

import java.util.Calendar;
import java.util.List;

public interface AccountService {

    void updateValue(Account account, Calendar updatedOn);

}
