package com.sharifdev.weather.network;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class InternetConnectionChecker {
    public static boolean checkConnection(String checkingSourceUrl) {
        try {
            URL url = new URL(checkingSourceUrl);
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
