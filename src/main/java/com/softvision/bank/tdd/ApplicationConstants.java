package com.softvision.bank.tdd;

public class ApplicationConstants {

    public static final String REGULAR = "regular";
    public static final String CHECKING = "checking";
    public static final String INTEREST = "interest";
    public static final String WITHDRAW = "WITHDRAW";
    public static final String DEPOSIT = "DEPOSIT";

    //put to property file?
    public static final double REG_PENALTY = 10;
    public static final double REG_MIN_BALANCE = 500;

    public static final double CHK_MIN_BALANCE = 100;
    public static final double CHK_PENALTY = 10;
    public static final double CHK_CHARGE = 1;

    public static final double INT_INTEREST = 0.03;
}
