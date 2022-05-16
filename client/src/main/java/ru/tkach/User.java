package ru.tkach;

import java.util.HashMap;
import java.util.Map;

public class User {
    private final int id;
    private final Map<String, Integer> stockAmounts;
    private int balance;

    public User(int id) {
        this.id = id;
        stockAmounts = new HashMap<>();
        balance = 0;
    }

    public int getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public Map<String, Integer> getStockAmounts() {
        return Map.copyOf(stockAmounts);
    }

    public int getStockAmount(String name) {
        return stockAmounts.getOrDefault(name, 0);
    }

    public void addToBalance(int amount) {
        balance += amount;
    }

    public void addStocks(String name, int amount) {
        stockAmounts.put(name, getStockAmount(name) + amount);
    }
}
