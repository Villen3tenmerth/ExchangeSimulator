package ru.tkach;

import java.util.HashMap;
import java.util.Map;

public class StockManager {
    private final Map<String, Stock> stocks;

    public StockManager() {
        stocks = new HashMap<>();
    }

    public void checkStock(String name) {
        if (!stocks.containsKey(name)) {
            throw new RuntimeException("No such stock: " + name);
        }
    }

    public boolean registerStock(String name, int amount, int price) {
        return stocks.putIfAbsent(name, new Stock(name, amount, price)) == null;
    }

    public int getPrice(String name) {
        checkStock(name);
        return stocks.get(name).getPrice();
    }

    public void setPrice(String name, int price) {
        checkStock(name);
        stocks.get(name).setPrice(price);
    }

    public int getAmount(String name) {
        checkStock(name);
        return stocks.get(name).getAmount();
    }

    public int buyStock(String name, int amount) {
        checkStock(name);
        if (getAmount(name) < amount) {
            throw new RuntimeException("Not enough stock to buy: " + name);
        }
        stocks.get(name).addAmount(-amount);
        return getPrice(name) * amount;
    }

    public int sellStock(String name, int amount) {
        checkStock(name);
        stocks.get(name).addAmount(amount);
        return getPrice(name) * amount;
    }
}
