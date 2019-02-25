package com.interview.task.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Class represents USD currency rate properties.
 */
@Configuration
@ConfigurationProperties(prefix = "currency.usd")
public class UsdCurrencyRateProperties {
    private Double uah;
    private Double eur;

    public Double getUah() {
        return uah;
    }

    public void setUah(Double uah) {
        this.uah = uah;
    }

    public Double getEur() {
        return eur;
    }

    public void setEur(Double eur) {
        this.eur = eur;
    }
}
