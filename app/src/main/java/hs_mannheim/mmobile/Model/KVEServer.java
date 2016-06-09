package hs_mannheim.mmobile.Model;

import java.util.Locale;

public class KVEServer {

    private static final String IP_KVE = "141.19.44.99";
    private static final String IP_UXID = "141.19.140.35";
    private static final String MESSAGE = "{\"inside\":\"%s\", \"color\":\"%s\"}";
    private static final int PORT = 9999;

    public void send(boolean inside, String color) {
        String message = String.format(Locale.GERMANY, MESSAGE, Boolean.toString(inside), color);
        new UDPClient(IP_KVE, PORT).send(message);
    }
}
