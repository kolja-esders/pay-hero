package edu.pietro.team.payhero.helper.api;


import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import edu.pietro.team.payhero.helper.GenericCallback;
import edu.pietro.team.payhero.helper.HttpClient;

public class ExchangeRatesAPI {

    private static final String BASE_URL = "http://api.fixer.io/";

    private static final HttpClient mClient = new HttpClient(BASE_URL);

    /**
     * Returns the most recent exchange rates with respect to the given base currency.
     * The exchange rates are updated daily around 4PM CET and are published by the
     * European Central Bank.
     *
     * @param baseCurrencySymbol Currency to return rates for. The format is case-insensitive and
     *                           expected to be a currency symbol like {@code USD} or {@code EUR}.
     * @return the most recent exchange rates for the given currency
     */
    public static void getRatesForCurrency(String baseCurrencySymbol,
                                           final GenericCallback<Map<String, Double>> callback) {
        mClient.get("latest?base=" + baseCurrencySymbol, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Map<String, Double> rates = new HashMap<>();
                try {
                    JSONObject jsonRates = response.getJSONObject("rates");
                    Iterator<String> keyIter = jsonRates.keys();
                    while (keyIter.hasNext()) {
                        String currencySymbol = keyIter.next();
                        Double rate = jsonRates.getDouble(currencySymbol);
                        rates.put(currencySymbol, rate);
                    }
                    callback.onSuccess(rates);
                } catch (JSONException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Throwable e = new RuntimeException(
                        String.format(Locale.ENGLISH, "Received status code %i with response %s.",
                                statusCode, responseString),
                        throwable);
                callback.onFailure(e);
            }
        });
    }

}
