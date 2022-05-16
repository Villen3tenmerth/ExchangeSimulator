package ru.tkach;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private final Map<Integer, User> users;
    private final ExchangeProvider exchangeProvider;

    public UserManager() {
        users = new HashMap<>();
        exchangeProvider = new ExchangeProvider("localhost:8080");
    }

    public void checkUser(int id) {
        if (!users.containsKey(id)) {
            throw new RuntimeException("User with id " + id + " doesn't exist");
        }
    }

    public boolean registerUser(int id) {
        return users.putIfAbsent(id, new User(id)) == null;
    }

    public void addToUserBalance(int id, int amount) {
        checkUser(id);
        users.get(id).addToBalance(amount);
    }

    public void buyStock(int userId, String stockName, int amount) {
        checkUser(userId);
        User user = users.get(userId);
        int price = exchangeProvider.buyStock(stockName, amount);
        if (user.getBalance() < price) {
            exchangeProvider.sellStock(stockName, amount);
            throw new RuntimeException("User's balance is not enough");
        }
        user.addToBalance(-price);
        user.addStocks(stockName, amount);
    }

    public void sellStock(int userId, String stockName, int amount) {
        checkUser(userId);
        User user = users.get(userId);
        if (user.getStockAmount(stockName) < amount) {
            throw new RuntimeException("User doesn't have enough stocks");
        }
        user.addStocks(stockName, -amount);
        user.addToBalance(exchangeProvider.sellStock(stockName, amount));
    }

    public int calcTotalValue(int userId) {
        checkUser(userId);
        User user = users.get(userId);
        int res = user.getBalance();
        for (var entry : user.getStockAmounts().entrySet()) {
            res += exchangeProvider.getStockPrice(entry.getKey()) * entry.getValue();
        }
        return res;
    }

    public String getUserInfo(int userId) {
        checkUser(userId);
        User user = users.get(userId);
        StringBuilder sb = new StringBuilder();
        for (var entry : user.getStockAmounts().entrySet()) {
            sb.append(entry.getKey())
                    .append(": amount=").append(entry.getValue())
                    .append(", price=").append(exchangeProvider.getStockPrice(entry.getKey()))
                    .append("\n");
        }
        return sb.toString();
    }
}
