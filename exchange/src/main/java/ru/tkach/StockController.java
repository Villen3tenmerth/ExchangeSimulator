package ru.tkach;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@RestController
public class StockController {
    private final StockManager stockManager = new StockManager();

    private String getStringOrError(Supplier<String> sup) {
        try {
            return sup.get();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping("/register")
    public String register(@RequestParam String name, @RequestParam int amount, @RequestParam int price) {
        if (stockManager.registerStock(name, amount, price)) {
            return "Stock " + name + " was successfully added";
        } else {
            return "Stock with this name already exists";
        }
    }

    @RequestMapping("/get_price")
    public String getPrice(@RequestParam String name) {
        return getStringOrError(() -> Integer.toString(stockManager.getPrice(name)));
    }

    @RequestMapping("/set_price")
    public String getPrice(@RequestParam String name, @RequestParam int price) {
        return getStringOrError(() -> {
            stockManager.setPrice(name, price);
            return "Price successfully changed";
        });
    }

    @RequestMapping("/get_amount")
    public String getAmount(@RequestParam String name) {
        return getStringOrError(() -> Integer.toString(stockManager.getAmount(name)));
    }

    @RequestMapping("/buy")
    public String buy(@RequestParam String name, @RequestParam int amount) {
        return getStringOrError(() -> Integer.toString(stockManager.buyStock(name, amount)));
    }

    @RequestMapping("/sell")
    public String sell(@RequestParam String name, @RequestParam int amount) {
        return getStringOrError(() -> Integer.toString(stockManager.sellStock(name, amount)));
    }
}
