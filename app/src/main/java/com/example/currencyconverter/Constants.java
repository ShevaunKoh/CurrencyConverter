package com.example.currencyconverter;

public class Constants {
    private static final String API_KEY="887d3dcdafeec1255d74";
    private static final String API_URL = "https://free.currconv.com/api/v7/";

    // all Currencies
    public static final String GET_CURRENCIES_API = API_URL + "currencies?apiKey=" + API_KEY;

    // All Countries
    public static final String GET_COUNTRIES_API = API_URL + "countries?apiKey=" + API_KEY;

    // Get Currency
    public static final String GET_CURRENCY_API = API_URL + "convert?apiKey=" + API_KEY; // &q=USD_PHP,PHP_USD&compact=ultra&date=2020-12-31<&endDate=2021-08-17>
    // For Single Date - &q=USD_PHP,PHP_USD&compact=ultra&date=2020-12-31
    // Date Range - &q=USD_PHP,PHP_USD&compact=ultra&date=2020-12-31&endDate=2021-08-17
    // Current - &q=USD_PHP&compact=y

    // Check Api usage
    public static final String GET_USAGE_API = "https://free.currconv.com/others/usage?apiKey=" + API_KEY;

}
