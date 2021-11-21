package com.example.currencyconverter.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.currencyconverter.MainActivity;
import com.example.currencyconverter.R;
import com.example.currencyconverter.model.Country;

import java.util.ArrayList;

public class CountryItemAdapter extends BaseAdapter {
    private ArrayList<Country> CountryList;
    Activity context;

    public CountryItemAdapter(MainActivity mainActivity, ArrayList<Country> cList) {
        super();
        context = mainActivity;
        CountryList = cList;
    }

    @Override
    public int getCount() {
        if(CountryList == null) {
            return 0;
        }
        else {
            return CountryList.size();
        }
    }

    @Override
    public Object getItem(int i) {
        if(CountryList == null) {
            return null;
        }
        else {
            return CountryList.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.country_list_item, null, true);

        TextView mCurrencyName = (TextView)rowView.findViewById(R.id.CurrencyName);
        TextView mCurrencyCode = (TextView)rowView.findViewById(R.id.CurrencyCode);
        TextView mCurrencySymbol = (TextView)rowView.findViewById(R.id.CurrencySymbol);
        // ImageView mFlag = (ImageView)rowView.findViewById(R.id.FlagImage);

        if(CountryList== null){
            Log.i("Get Object from json", "No records found");
        }
        else {
            // Set Text to spinner
            mCurrencyName.setText(CountryList.get(i).getCurrencyName());
            mCurrencyCode.setText(CountryList.get(i).getCurrencyCode());
            mCurrencySymbol.setText(CountryList.get(i).getCurrencySymbol());
        }
        return rowView;
    }

}
