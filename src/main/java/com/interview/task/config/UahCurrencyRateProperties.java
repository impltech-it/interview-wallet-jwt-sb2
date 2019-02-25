package com.interview.task.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Class represents UAH currency rate properties.
 */
@Configuration
@ConfigurationProperties(prefix = "currency.uah")
public class UahCurrencyRateProperties {
    private Double usd;
    private Double eur;

    public Double getUsd() {
        return usd;
    }

    public void setUsd(Double usd) {
        this.usd = usd;
    }

    public Double getEur() {
        return eur;
    }

    public void setEur(Double eur) {
        this.eur = eur;
    }
}
