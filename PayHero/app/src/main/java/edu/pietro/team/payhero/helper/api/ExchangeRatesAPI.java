package edu.pietro.team.payhero.helper.api;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;
import edu.pietro.team.payhero.helper.AbstractCache;
import edu.pietro.team.payhero.helper.GenericCallbackInterface;
import edu.pietro.team.payhero.helper.HttpClient;

public class ExchangeRatesAPI {

    private static final String BASE_URL = "http://api.fixer.io/";

    private static final String LATEST_RATES_WITH_BASE_RESOURCE = "latest?base=";

    private static final HttpClient HTTP_CLIENT = new HttpClient(BASE_URL);

    private static final SimpleDateFormat CACHE_KEY_DATE_FORMAT
            = new SimpleDateFormat("yyyy-MM-dd");

    private AbstractCache<HashMap<Currency,Double>> mCache;

    public ExchangeRatesAPI() {
    }

    public ExchangeRatesAPI(AbstractCache<HashMap<Currency,Double>> cache) {
        mCache = cache;
    }

    /**
     * Returns the most recent exchange rates with respect to the given base currency.
     * The exchange rates are updated daily around 4PM CET and are published by the
     * European Central Bank.
     *
     * @param baseCurrency to return rates for.
     * @param callback to be called on success/failure.
     */
    public void getRatesForCurrency(final Currency baseCurrency,
                                    final GenericCallbackInterface<HashMap<Currency,Double>>callback) {
        if (mCache != null) {
            try {
                HashMap<Currency, Double> rates = mCache.get(constructCacheKey(baseCurrency));
                if (rates != null) {
                    callback.onSuccess(rates);
                    return;
                }
            } catch (IOException e) {
                // Just fetch the stuff below.
            }
        }
        HTTP_CLIENT.get(LATEST_RATES_WITH_BASE_RESOURCE + baseCurrency.getCurrencyCode(),
                null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                HashMap<Currency, Double> rates = new HashMap<>();
                try {
                    JSONObject jsonRates = response.getJSONObject("rates");
                    Iterator<String> keyIter = jsonRates.keys();
                    while (keyIter.hasNext()) {
                        String currencyCode = keyIter.next();
                        Double rate = jsonRates.getDouble(currencyCode);
                        rates.put(Currency.getInstance(currencyCode), rate);
                    }
                    try {
                        mCache.put(constructCacheKey(baseCurrency), rates);
                    } catch (IOException e) {
                        // Poor cache :(
                    }
                    callback.onSuccess(rates);
                } catch (JSONException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                Throwable e = new RuntimeException(
                        String.format(Locale.ENGLISH, "Received status code %i with response %s.",
                                statusCode, responseString),
                        throwable);
                callback.onFailure(e);
            }
        });
    }

    private String constructCacheKey(Currency baseCurrency) {
        // Since the exchange rates are updated _around_ 16:00 CET, we will factor in a small buffer
        // of 30 minutes. If it is at least 16:10 CET we will get the new exchange rates.
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        if (date.get(Calendar.HOUR_OF_DAY) < 16 ||
                (date.get(Calendar.HOUR_OF_DAY) == 16 && date.get(Calendar.MINUTE) < 10)) {
            date.add(Calendar.DATE, -1);
        }
        return StringUtils.join(CACHE_KEY_DATE_FORMAT.format(date.getTime()),
                "_", baseCurrency.getCurrencyCode());
    }
}
