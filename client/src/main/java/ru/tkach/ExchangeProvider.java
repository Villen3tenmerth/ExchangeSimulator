package ru.tkach;

import org.springframework.web.util.UriComponentsBuilder;

public class ExchangeProvider {
    private final String host;

    public ExchangeProvider(String host) {
        this.host = host;
    }

    public int getStockPrice(String name) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
                .path("/get_price")
                .queryParam("name", name)
                .build().toUriString();
        return Integer.parseInt(UrlUtils.sendThenReceive(url));
    }

    public int buyStock(String name, int amount) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
                .path("/buy")
                .queryParam("name", name)
                .queryParam("amount", amount)
                .build().toUriString();
        String response = UrlUtils.sendThenReceive(url);
        try {
            return Integer.parseInt(response);
        } catch (NumberFormatException e) {
            throw new RuntimeException(response);
        }
    }

    public int sellStock(String name, int amount) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
                .path("/sell")
                .queryParam("name", name)
                .queryParam("amount", amount)
                .build().toUriString();
        return Integer.parseInt(UrlUtils.sendThenReceive(url));
    }
}
