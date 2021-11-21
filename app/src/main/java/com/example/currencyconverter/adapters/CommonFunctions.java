package com.example.currencyconverter.adapters;

import android.util.Log;

import com.example.currencyconverter.model.Country;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommonFunctions {

    final class CountryResult{
        public HashMap<String, Country> results;

        public HashMap<String, Country> getResult() {
            return results;
        }
    }



    public static Object GetObjectFromJson(String s, Object o){
        Gson gson = new Gson();
        Object object = new Object();
        try{
            object = gson.fromJson(s, o.getClass());

        } catch(Exception e){
            Log.e("Get Object from json", "GetObjectFromJson: ", e);
        }

        return object;
    }

    public static ArrayList<Country> GetCountryListFromJson(String jsonString){
        Gson gson = new GsonBuilder().create();
        CountryResult CountryListResult;
        ArrayList<Country> CountryList = new ArrayList<Country>();
        // Log.i("GetCountryListFromJson", "json: "+ jsonString);

        try{
            // Cast json into CountryResult Object
            CountryListResult = gson.fromJson(jsonString, new TypeToken<CountryResult>(){}.getType());

            // Foreach Hashmap entry in results, add to country list
            for (Map.Entry<String, Country> entry : CountryListResult.getResult().entrySet()) {
                // Log.i("GetCountryListFromJson", "GetCountryName: "+ entry.getValue().getCountryName());
                CountryList.add(entry.getValue());
            }
            // Log.i("GetCountryListFromJson", "GetCountrysize: "+ CountryList.size());

        } catch(Exception e){
            Log.e("GetCountryListFromJson", "GetCountryListFromJson: ", e);
        }
        return CountryList;
    }

    public static Float GetExchangeRateFromJson(String jsonString){
        Gson gson = new Gson();
        Map<String, Float> ExchangeRateMap;
        Float ExchangeRate=new Float(0);
        // Log.i("GetExchangeRateFromJson", "Json: "+ jsonString);
        try{
            ExchangeRateMap = gson.fromJson(jsonString, new TypeToken<Map<String, Float>>(){}.getType());
            // Log.i("GetExchangeRateFromJson", "ExchangeRateMap: "+ ExchangeRateMap);

            // Foreach Hashmap entry in Mapping
            for (Map.Entry<String, Float> entry : ExchangeRateMap.entrySet()) {
                // Log.i("GetExchangeRateFromJson", "ExchangeRateMap: "+ entry.getValue());
                ExchangeRate=entry.getValue();
            }
        } catch(Exception e){
            Log.e("GetExchangeRateFromJson", "GetExchangeRateFromJson: ", e);
        }
        return ExchangeRate;
    }
}
