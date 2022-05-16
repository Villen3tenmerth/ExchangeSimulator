package ru.tkach;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtils {
    public static String sendThenReceive(String urlString) {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
