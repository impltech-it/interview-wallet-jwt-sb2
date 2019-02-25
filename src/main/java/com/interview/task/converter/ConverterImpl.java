package com.interview.task.converter;

import com.interview.task.config.EurCurrencyRateProperties;
import com.interview.task.config.UahCurrencyRateProperties;
import com.interview.task.config.UsdCurrencyRateProperties;
import com.interview.task.enums.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class represents simple currency converter functionality.
 */
@Component
public class ConverterImpl implements Converter {

    private final UahCurrencyRateProperties uahCurrencyRateProperties;
    private final UsdCurrencyRateProperties usdCurrencyRateProperties;
    private final EurCurrencyRateProperties eurCurrencyRateProperties;

    @Autowired
    public ConverterImpl(
            final UahCurrencyRateProperties uahCurrencyRateProperties,
            final UsdCurrencyRateProperties usdCurrencyRateProperties,
            final EurCurrencyRateProperties eurCurrencyRateProperties) {
        this.uahCurrencyRateProperties = uahCurrencyRateProperties;
        this.usdCurrencyRateProperties = usdCurrencyRateProperties;
        this.eurCurrencyRateProperties = eurCurrencyRateProperties;
    }

    /**
     * Method performs currency convertation.
     *
     * @param amount money amount
     * @param convertFrom convert from this currency
     * @param convertTo convert to this currency
     * @return converted money amount
     */
    @Override
    public Double convert(final Double amount, final Currency convertFrom, final Currency convertTo) {
        double result = 0.0;
        switch (convertFrom.getTypeValue()) {
            case "UAH":
                result = getResult(amount, convertTo, "USD",
                        uahCurrencyRateProperties.getUsd(), uahCurrencyRateProperties.getEur());
                break;
            case "USD":
                result = getResult(amount, convertTo, "UAH",
                        usdCurrencyRateProperties.getUah(), usdCurrencyRateProperties.getEur());
                break;
            case "EUR":
                result = getResult(amount, convertTo, "UAH",
                        eurCurrencyRateProperties.getUah(), eurCurrencyRateProperties.getUsd());
                break;
        }
        return result;
    }

    /**
     * Helpful method for convertation processing.
     * Every currency in this application has two currency rates.
     *
     * @param amount money amount
     * @param convertTo convert from this currency
     * @param currencyType convert to this currency
     * @param currencyFirstRate first currency rate
     * @param currencyRemainingRate remaining currency rate
     * @return processed result
     */
    private double getResult(
            final Double amount,
            final Currency convertTo,
            final String currencyType,
            final Double currencyFirstRate,
            final Double currencyRemainingRate) {
        double result;
        if (convertTo.getTypeValue().equals(currencyType)) {
            result = amount * currencyFirstRate;
        } else {
            result = amount * currencyRemainingRate;
        }
        return result;
    }

}
