package ru.tkach;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@RestController
public class UserController {
    private final UserManager userManager = new UserManager();

    private String getStringOrError(Supplier<String> sup) {
        try {
            return sup.get();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping("/register")
    public String register(@RequestParam int userId) {
        if (userManager.registerUser(userId)) {
            return "User was successfully added";
        } else {
            return "User with id " + userId + " already exists";
        }
    }

    @RequestMapping("/deposit")
    public String deposit(@RequestParam int userId, @RequestParam int amount) {
        return getStringOrError(() -> {
            userManager.addToUserBalance(userId, amount);
            return "Deposit complete";
        });
    }

    @RequestMapping("/buy")
    public String buy(@RequestParam int userId, @RequestParam String stockName, @RequestParam int amount) {
        return getStringOrError(() -> {
            userManager.buyStock(userId, stockName, amount);
            return "Purchase complete";
        });
    }

    @RequestMapping("/sell")
    public String sell(@RequestParam int userId, @RequestParam String stockName, @RequestParam int amount) {
        return getStringOrError(() -> {
            userManager.sellStock(userId, stockName, amount);
            return "Sale complete";
        });
    }

    @RequestMapping("/total")
    public String total(@RequestParam int userId) {
        return getStringOrError(() -> Integer.toString(userManager.calcTotalValue(userId)));
    }

    @RequestMapping("/info")
    public String info(@RequestParam int userId) {
        return getStringOrError(() -> userManager.getUserInfo(userId));
    }
}
