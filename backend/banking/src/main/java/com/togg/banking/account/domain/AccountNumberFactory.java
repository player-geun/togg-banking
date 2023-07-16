package com.togg.banking.account.domain;

import java.util.Random;

public class AccountNumberFactory {

    private final static String BANKBOOK_NUMBER = "1000";
    private final static Random RANDOM = new Random();

    public static String createNumber() {
        int number = RANDOM.nextInt(100_000_000);
        return BANKBOOK_NUMBER + number;
    }
}
