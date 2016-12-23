package edu.pietro.team.payhero.helper;


import java.util.Currency;
import java.util.HashMap;

import edu.pietro.team.payhero.entities.AmountOfMoney;
import edu.pietro.team.payhero.helper.api.ExchangeRatesAPI;
import edu.pietro.team.payhero.helper.api.InternalStorageCache;

public class CurrencyHelper {

    private static final String CACHE_PREFIX = "EXCHANGE_RATES";

    private static final AbstractCache<HashMap<String, Double>> CACHE =
            new InternalStorageCache<HashMap<String, Double>>(null, CACHE_PREFIX);

    private static final ExchangeRatesAPI exchangeRates = new ExchangeRatesAPI();

    public static AmountOfMoney convert(AmountOfMoney amount, Currency newCurrency) {

        exchangeRates.getRatesForCurrency(amount.getCurrency(),
                new GenericCallbackInterface<HashMap<Currency, Double>>() {
            @Override
            public void onSuccess(HashMap<Currency, Double> result) {

            }
            @Override
            public void onFailure(Throwable e) {
            }
        });

        return null;
    }

}
