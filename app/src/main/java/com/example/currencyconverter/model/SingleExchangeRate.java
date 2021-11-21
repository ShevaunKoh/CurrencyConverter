package com.example.currencyconverter.model;

import com.google.gson.annotations.SerializedName;

public class SingleExchangeRate {
    @SerializedName("val")
    float CurrencyRate;

    public float getCurrencyRate() {
        return CurrencyRate;
    }
}
