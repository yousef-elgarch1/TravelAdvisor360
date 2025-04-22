package com.example.traveladvisor360.utils;


import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CurrencyUtils {

    public static String formatCurrency(double amount, String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        format.setCurrency(currency);
        return format.format(amount);
    }

    public static String getCurrencySymbol(String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        return currency.getSymbol();
    }

    public static double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        // In a real app, this would use a currency conversion API
        // For demo purposes, let's use static conversion rates
        double rate = getExchangeRate(fromCurrency, toCurrency);
        return amount * rate;
    }

    private static double getExchangeRate(String fromCurrency, String toCurrency) {
        // Mock exchange rates (USD as base)
        if (fromCurrency.equals("USD")) {
            switch (toCurrency) {
                case "EUR": return 0.85;
                case "GBP": return 0.73;
                case "JPY": return 110.0;
                case "AUD": return 1.34;
                case "CAD": return 1.25;
                default: return 1.0;
            }
        } else if (toCurrency.equals("USD")) {
            switch (fromCurrency) {
                case "EUR": return 1.18;
                case "GBP": return 1.37;
                case "JPY": return 0.0091;
                case "AUD": return 0.75;
                case "CAD": return 0.80;
                default: return 1.0;
            }
        }
        return 1.0;
    }

    public static String formatPrice(double price, String currencyCode) {
        if (currencyCode == null || currencyCode.isEmpty()) {
            currencyCode = "USD";
        }
        return formatCurrency(price, currencyCode);
    }
}
