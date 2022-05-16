package ru.tkach;

public class Stock {
    private final String name;
    private int amount;
    private int price;

    public Stock(String name, int amount, int price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public void addAmount(int delta) {
        amount += delta;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
