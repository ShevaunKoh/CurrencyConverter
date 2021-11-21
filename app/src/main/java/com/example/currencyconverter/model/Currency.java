package com.example.currencyconverter.model;

import com.google.gson.annotations.SerializedName;

public class Currency {
    @SerializedName("currencyName")
    String CurrencyName;

    @SerializedName("currencySymbol")
    String CurrencySymbol;

    @SerializedName("id")
    String CurrencyCode;

    public String getCurrencyName() {
        return CurrencyName;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public String getCurrencySymbol() {
        return CurrencySymbol;
    }
}
