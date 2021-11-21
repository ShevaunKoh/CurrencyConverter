package com.example.currencyconverter;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.currencyconverter.adapters.CommonFunctions;
import com.example.currencyconverter.adapters.CountryItemAdapter;
import com.example.currencyconverter.model.Country;
import com.example.currencyconverter.model.CurrencyRangeResult;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.example.currencyconverter.Constants.GET_COUNTRIES_API;
import static com.example.currencyconverter.Constants.getCurrencyRangeAPIString;
import static com.example.currencyconverter.Constants.getExchangeRateAPIString;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    // Initialise UI Elements
    private TextView tv_CurrencyNameFromLabel;
    private TextView tv_CurrencyNameToLabel;
    private Spinner sp_CurrencyNameFromSpinner;
    private Spinner sp_CurrencyNameToSpinner;
    private EditText et_CurrencyAmtFromEditText;
    private EditText et_CurrencyAmtToEditText;
    private LineChart ct_LineChart;
    ArrayList<Country> ListOfCurrency = new ArrayList<>();

    // Initialise selected Variables from user inputs
    String SelectedCurrencyCodeFrom="", SelectedCurrencyCodeTo="", SelectedCurrencyNameTo="";
    double CurrencyAmtFrom=0, CurrencyAmtTo=0;
    Float ExchangeRateResult=new Float(0);
    boolean sysChanged=false; // Indicate system Changes
    boolean sysCurrencyTo=true; // Indicate if conversion is from To


/*    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
            }
            return false;
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //BottomNavigationView navView = findViewById(R.id.nav_view);
        //navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Bind UI Elements to objects
        tv_CurrencyNameFromLabel = (TextView) findViewById(R.id.CurrencyNameFromLabel);
        tv_CurrencyNameToLabel = (TextView) findViewById(R.id.CurrencyNameToLabel);
        sp_CurrencyNameFromSpinner = (Spinner) findViewById(R.id.CurrencyNameFromSpinner);
        sp_CurrencyNameToSpinner = (Spinner) findViewById(R.id.CurrencyNameToSpinner);
        et_CurrencyAmtFromEditText = (EditText) findViewById(R.id.CurrencyAmtFromEditText);
        et_CurrencyAmtToEditText = (EditText) findViewById(R.id.CurrencyAmtToEditText);
        ct_LineChart = (LineChart) findViewById(R.id.chart1);
        new getAllCountries().execute(GET_COUNTRIES_API);

        sp_CurrencyNameFromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                //Get selected currency
                if(pos != -1){
                    SelectedCurrencyCodeFrom = ListOfCurrency.get(pos).getCurrencyCode();

                    // Set Currency From Amt
                    CurrencyAmtFrom=Double.parseDouble(et_CurrencyAmtFromEditText.getText().toString());

                    // Set Currency From Amt to 0 for updates from exchange rate
                    CurrencyAmtTo=0;
                    sysCurrencyTo=false;

                    // Set Label for display
                    tv_CurrencyNameFromLabel.setText(String.format("1 %s equals", ListOfCurrency.get(pos).getCurrencyName()));
                    new getCurrencyRange().execute(getCurrencyRangeAPIString(SelectedCurrencyCodeFrom,SelectedCurrencyCodeTo));

                    updateExchangeRate();
                    // Toast.makeText(MainActivity.this, "Code: " + SelectedCurrencyCodeFrom, Toast.LENGTH_LONG).show();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        sp_CurrencyNameToSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                //Get selected currency
                if(pos != -1){
                    SelectedCurrencyCodeTo = ListOfCurrency.get(pos).getCurrencyCode();

                    // Set Currency From Amt
                    CurrencyAmtFrom=Double.parseDouble(et_CurrencyAmtFromEditText.getText().toString());

                    // Set Currency From Amt to 0 for updates from exchange rate
                    CurrencyAmtTo=0;
                    sysCurrencyTo=false;

                    // Set Label for display
                    SelectedCurrencyNameTo=ListOfCurrency.get(pos).getCurrencyName();
                    new getCurrencyRange().execute(getCurrencyRangeAPIString(SelectedCurrencyCodeFrom,SelectedCurrencyCodeTo));

                    updateExchangeRate();
                    // Toast.makeText(MainActivity.this, "Code: " + SelectedCurrencyCodeTo, Toast.LENGTH_LONG).show();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        et_CurrencyAmtFromEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // Get Currency To Amt
                if(!sysChanged && et_CurrencyAmtFromEditText.hasFocus()){
                    if(et_CurrencyAmtFromEditText.getText().toString().isEmpty()){
                        CurrencyAmtFrom=0;
                    }else{
                        CurrencyAmtFrom=Double.parseDouble(et_CurrencyAmtFromEditText.getText().toString());
                    }
                    // Set Currency From Amt to 0 for updates from exchange rate
                    CurrencyAmtTo=0;
                    sysCurrencyTo=false;

                    updateExchangeRate();
                } else{
                    sysChanged=false;
                }

            }
        });

        et_CurrencyAmtToEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!sysChanged && et_CurrencyAmtToEditText.hasFocus()){
                    Log.i("onTextChanged", "et_CurrencyAmtToEditText in if");
                    // Get Currency To Amt
                    if(et_CurrencyAmtToEditText.getText().toString().isEmpty()){
                        CurrencyAmtTo=0;
                    }else{
                        CurrencyAmtTo=Double.parseDouble(et_CurrencyAmtToEditText.getText().toString());
                    }

                    // Set Currency From Amt to 0 for updates from exchange rate
                    CurrencyAmtFrom=0;
                    sysCurrencyTo=true;

                    updateExchangeRate();
                } else{
                    sysChanged=false;
                }
            }
        });

    }
    public void updateExchangeRate() {
        // Toast.makeText(MainActivity.this, "Code: " + Double.toString(CurrencyAmtFrom)+ Double.toString(CurrencyAmtTo), Toast.LENGTH_LONG).show();
        if(SelectedCurrencyCodeFrom.isEmpty()||SelectedCurrencyCodeTo.isEmpty()){
            // A Currency is empty - To skip
            return;
        }
        if(CurrencyAmtFrom==0 && CurrencyAmtTo==0){
            // No Amt on both from and to - To skip
            return;
        }

        if(!sysCurrencyTo){
            // To get exchange rate --> From - To
            Log.i("onTextChanged", "updateExchangeRate from");
            new getExchangeRate().execute(getExchangeRateAPIString(SelectedCurrencyCodeFrom,SelectedCurrencyCodeTo));
            return;
        }
        if(sysCurrencyTo){
            // To get exchange rate --> To - From (only from Edittext updates)
            Log.i("onTextChanged", "updateExchangeRate to");
            new getExchangeRate().execute(getExchangeRateAPIString(SelectedCurrencyCodeTo,SelectedCurrencyCodeFrom));
        }
    }

    // Get list of Countries for Spinner
    protected class getAllCountries extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... url) {

            try {
                URL Url = new URL(url[0]);
                URLConnection urlConnection = Url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String str = "";
                str = bufferedReader.readLine();
                return str;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ArrayList<Country> CountryList = new ArrayList<Country>();
            CountryList = CommonFunctions.GetCountryListFromJson(result);

            // Sort list by currency name
            Collections.sort(CountryList, new Comparator<Country>() {
                public int compare(Country c1, Country c2) {
                    // compare two instance of `Country` and return in Ascending order
                    return c1.getCurrencyName().compareTo(c2.getCurrencyName());
                }
            });

            ListOfCurrency.clear(); // Clear List before Adding

            // Append list of Countries
            if(CountryList != null){
                for(int i=0; i<CountryList.size(); i++){
                    boolean CurrencyExists = false;
                    for(int c = 0; c< ListOfCurrency.size(); c++){
                        if(ListOfCurrency.get(c).CurrencyEquals(CountryList.get(i))){
                            CurrencyExists=true;
                            break;
                        }
                    }
                    if(!CurrencyExists){
                        ListOfCurrency.add(CountryList.get(i));
                    }
                }
            }

            // Set Adapter to Spinners
            CountryItemAdapter CurrencyAdapter = new CountryItemAdapter(MainActivity.this, ListOfCurrency);
            sp_CurrencyNameFromSpinner.setAdapter(CurrencyAdapter);
            sp_CurrencyNameToSpinner.setAdapter(CurrencyAdapter);

        }
    }


    // Get list of Countries for Spinner
    protected class getExchangeRate extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... url) {

            try {
                URL Url = new URL(url[0]);
                URLConnection urlConnection = Url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String str = "";
                str = bufferedReader.readLine();
                return str;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ExchangeRateResult = CommonFunctions.GetExchangeRateFromJson(result);
            // Log.i("getExchangeRate", "ExchangeRate: "+ ExchangeRateResult);
            sysChanged=true;
            DecimalFormat df = new DecimalFormat("#.##");
            if(!sysCurrencyTo){
                Log.i("onTextChanged", "onPostExecute from");
                CurrencyAmtTo=ExchangeRateResult*CurrencyAmtFrom;
                et_CurrencyAmtToEditText.setText(df.format(CurrencyAmtTo));
                tv_CurrencyNameToLabel.setText(String.format("%s %s", ExchangeRateResult, SelectedCurrencyNameTo));
                return;
            }
            if(sysCurrencyTo){
                Log.i("onTextChanged", "onPostExecute to");
                CurrencyAmtFrom=ExchangeRateResult*CurrencyAmtTo;
                et_CurrencyAmtFromEditText.setText(df.format(CurrencyAmtFrom));
                // tv_CurrencyNameToLabel.setText(String.format("%s %s", ExchangeRateResult, SelectedCurrencyNameTo));
            }

        }
    }


    // Get list of Countries for Chart
    protected class getCurrencyRange extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... url) {

            try {
                URL Url = new URL(url[0]);
                URLConnection urlConnection = Url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String str = "";
                str = bufferedReader.readLine();
                return str;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            ArrayList<CurrencyRangeResult> CurrencyRangeList = new ArrayList<CurrencyRangeResult>();
            CurrencyRangeList = CommonFunctions.GetCurrencyRangeFromJson(result);
            if(CurrencyRangeList.size()<1){
                return;
            }

            ArrayList<Entry> values = new ArrayList<>();
            for (int i = 0; i < CurrencyRangeList.size(); i++) {
                // Add to Graph values
                values.add(new Entry(i, CurrencyRangeList.get(i).getCurrency()));
            }


            // Set up Graph
            LineDataSet set1 = new LineDataSet(values, CurrencyRangeList.get(0).getCurrencyDate() + " - " +CurrencyRangeList.get(CurrencyRangeList.size()-1).getCurrencyDate());
            set1.setLineWidth(1.75f);
            set1.setCircleRadius(5f);
            set1.setCircleHoleRadius(2.5f);
            set1.setColor(Color.WHITE);
            set1.setCircleColor(Color.WHITE);
            set1.setHighLightColor(Color.WHITE);
            set1.setDrawValues(true);
            set1.setValueTextColor(Color.WHITE);

            // Create Line Data
            LineData data = new LineData(set1);

            setupChart(ct_LineChart, data, Color.rgb(46, 183, 232));
        }
    }

    private void setupChart(LineChart chart, LineData data, int color) {

        ((LineDataSet) data.getDataSetByIndex(0)).setCircleHoleColor(color);

        // no description text
        chart.getDescription().setEnabled(false);

        // chart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(false);
//        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setBackgroundColor(color);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setViewPortOffsets(10, 0, 10, 0);

        // add data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setSpaceTop(40);
        chart.getAxisLeft().setSpaceBottom(40);
        chart.getAxisRight().setEnabled(false);

        chart.getXAxis().setEnabled(false);

        // animate calls invalidate()...
        chart.animateX(2500);
    }

}
