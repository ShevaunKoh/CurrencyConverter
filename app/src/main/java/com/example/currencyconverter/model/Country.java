package com.example.currencyconverter.model;

import com.google.gson.annotations.SerializedName;


public class Country {

    @SerializedName("alpha3")
    String Alpha3CountryCode;

    @SerializedName("currencyName")
    String CurrencyName;

    @SerializedName("currencyId")
    String CurrencyCode;

    @SerializedName("currencySymbol")
    String CurrencySymbol;

    @SerializedName("id")
    String CountryCode;

    @SerializedName("name")
    String CountryName;

    public String getCurrencyName() {
        return CurrencyName;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public String getCurrencySymbol() {
        return CurrencySymbol;
    }

    public String getAlpha3CountryCode() {
        return Alpha3CountryCode;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public String getCountryName() {
        return CountryName;
    }

    public boolean CurrencyEquals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country)) return false;

        Country country = (Country) o;

        if (!CurrencyName.equals(country.getCurrencyName())) return false;
        if (CurrencyName != null ? !CurrencyName.equals(country.getCurrencyName()) : country.getCurrencyName() != null) return false;

        return true;
    }
}
