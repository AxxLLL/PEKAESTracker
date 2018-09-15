package model.tracker.net;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Connector {
    private static final String trackerURL = "https://www.pekaes.pl/pl/dla-klienta.html";
    private static String lastPHPSessId = null;
    private static String lastRequestTokenNumber = null;

    public Document getShippingData(String shNumber) throws IOException {
        Connection conn = Jsoup.connect(trackerURL);
        Document document;
        if(lastPHPSessId == null || lastRequestTokenNumber == null) {
            conn = newConnection();
        }
        try {
            document = tryGetShippingDataFromSite(conn, shNumber);
        } catch(IOException | NullPointerException e) {
            conn = newConnection();
            document = tryGetShippingDataFromSite(conn, shNumber);
        }
        return document;
    }

    private Connection newConnection() throws IOException {
        lastPHPSessId = null;
        lastRequestTokenNumber = null;

        Connection conn = Jsoup.connect(trackerURL);
        conn.method(org.jsoup.Connection.Method.GET);
        Connection.Response response = conn.execute();

        lastPHPSessId = getPhpSessionId(response);
        lastRequestTokenNumber = getTokenNumber(response.parse());
        return conn;
    }

    private Document tryGetShippingDataFromSite(Connection conn, String shNumber) throws IOException {
        conn.cookie("PHPSESSID", lastPHPSessId);
        conn.data("REQUEST_TOKEN", lastRequestTokenNumber);
        conn.data("ShNumber", shNumber);
        return conn.post();
    }

    static private String getPhpSessionId(Connection.Response response) {
        return response.cookie("PHPSESSID");
    }

    static private String getTokenNumber(Document document) {
        return document.getElementById("formularz-wyszukiwania-przesylki").getElementsByAttributeValue("name", "REQUEST_TOKEN").first().attr("value");
    }
}
