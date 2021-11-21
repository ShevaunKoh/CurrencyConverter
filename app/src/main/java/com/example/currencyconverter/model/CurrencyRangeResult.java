package com.example.currencyconverter.model;

public class CurrencyRangeResult {

    String CurrencyDate;

    Float Currency;

    public CurrencyRangeResult(String currencyDate, Float currency) {
        CurrencyDate = currencyDate;
        Currency = currency;
    }

    public String getCurrencyDate() {
        return CurrencyDate;
    }

    public Float getCurrency() {
        return Currency;
    }

}
