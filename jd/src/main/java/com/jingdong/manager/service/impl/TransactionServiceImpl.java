package com.jingdong.manager.service.impl;

import com.jingdong.manager.command.TransactionCommand;
import com.jingdong.manager.service.TransactionService;

public class TransactionServiceImpl implements TransactionService {

    private TransactionCommand transactionCommand = new TransactionCommand();

    @Override
    public Long todayOrderTotal() {
        return transactionCommand.todayOrderTotal();
    }

    @Override
    public Long todayOrderValues() {
        return transactionCommand.todayOrderValues();
    }

    @Override
    public Long yesterdayOrderValues() {
        return transactionCommand.yesterdayOrderValues();
    }
}
