package ru.tkach;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.assertEquals;

@Testcontainers
public class ExchangeTest {
    private final static int EXCHANGE_PORT = 8080;
    private final static int CLIENT_PORT = 8081;
    private ConfigurableApplicationContext userServer;

    @Container
    private final static GenericContainer<?> EXCHANGE
            = new FixedHostPortGenericContainer<>("exchange:1.0-SNAPSHOT")
            .withFixedExposedPort(EXCHANGE_PORT, EXCHANGE_PORT).withExposedPorts(EXCHANGE_PORT);

    @Before
    public void beforeTest() {
        EXCHANGE.start();
        userServer = Application.run(new String[0]);
    }

    @After
    public void afterTest() {
        EXCHANGE.stop();
        userServer.stop();
    }

    private String sendThenReceive(int port, String path, String params) {
        return UrlUtils.sendThenReceive("http://localhost:" + port + "/" + path + "?" + params);
    }

    private String addStock(String name, int amount, int price) {
        return sendThenReceive(EXCHANGE_PORT, "register", "name=" + name + "&amount=" + amount + "&price=" + price);
    }

    private String addUser(int userId) {
        return sendThenReceive(CLIENT_PORT, "register", "userId=" + userId);
    }

    private String deposit(int userId, int amount) {
        return sendThenReceive(CLIENT_PORT, "deposit", "userId=" + userId + "&amount=" + amount);
    }

    private String buy(int userId, String stockName, int amount) {
        return sendThenReceive(CLIENT_PORT, "buy", "userId=" + userId + "&stockName=" + stockName + "&amount=" + amount);
    }

    private String sell(int userId, String stockName, int amount) {
        return sendThenReceive(CLIENT_PORT, "sell", "userId=" + userId + "&stockName=" + stockName + "&amount=" + amount);
    }

    private String total(int userId) {
        return sendThenReceive(CLIENT_PORT, "total", "userId=" + userId);
    }

    private String getPrice(String stockName) {
        return sendThenReceive(EXCHANGE_PORT, "get_price", "name=" + stockName);
    }

    private String setPrice(String stockName, int price) {
        return sendThenReceive(EXCHANGE_PORT, "set_price", "name=" + stockName + "&price=" + price);
    }

    private String getAmount(String stockName) {
        return sendThenReceive(EXCHANGE_PORT, "get_amount", "name=" + stockName);
    }

    private String getInfo(int userId) {
        return sendThenReceive(CLIENT_PORT, "info", "userId=" + userId);
    }

    @Test
    public void test() {
        String response;

        response = addStock("XXX", 10, 100);
        assertEquals("Stock XXX was successfully added", response);
        response = addStock("XXX", 10, 100);
        assertEquals("Stock with this name already exists", response);
        response = addStock("YYY", 20, 100);
        assertEquals("Stock YYY was successfully added", response);

        response = getPrice("XXX");
        assertEquals("100", response);
        response = setPrice("YYY", 50);
        assertEquals("Price successfully changed", response);
        response = getAmount("XXX");
        assertEquals("10", response);

        response = addUser(1);
        assertEquals("User was successfully added", response);
        response = addUser(1);
        assertEquals("User with id 1 already exists", response);
        response = addUser(2);
        assertEquals("User was successfully added", response);

        response = deposit(1, 500);
        assertEquals("Deposit complete", response);
        response = buy(1, "XXX", 5);
        assertEquals("Purchase complete", response);
        response = sell(1, "XXX", 3);
        assertEquals("Sale complete", response);

        buy(1, "YYY", 3);
        response = total(1);
        assertEquals("500", response);
        response = getInfo(1);
        assertEquals("YYY: amount=3, price=50XXX: amount=2, price=100", response);

        response = getInfo(10);
        assertEquals("User with id 10 doesn't exist", response);
        response = getPrice("ZZZ");
        assertEquals("Stock with name ZZZ doesn't exist", response);

        response = buy(1, "XXX", 5);
        assertEquals("User's balance is not enough", response);
        response = sell(1, "XXX", 5);
        assertEquals("User doesn't have enough stocks", response);
        deposit(1, 10000);
        response = buy(1, "XXX", 20);
        assertEquals("Not enough stock to buy: XXX", response);
    }
}
