package model.tracker.net;

import com.google.common.base.Preconditions;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Connector {
    private static final String TRACKER_URL = "https://www.pekaes.pl/pl/dla-klienta.html";
    private static String lastPHPSessionId = null;
    private static String lastRequestTokenNumber = null;
    private Document documentData;

    public Connector(String shNumber) throws IOException {
        Preconditions.checkNotNull(shNumber, "Brak numeru przesyłki");
        documentData = tryGetShippingDataFromSite(getConnection(), shNumber);
    }

    public Document getShippingData() {
        return documentData;
    }

    private Connection getConnection() throws IOException {
        return isSessionEstablishedBefore() ? Jsoup.connect(TRACKER_URL) : newConnection();
    }

    private Connection newConnection() throws IOException {
        lastPHPSessionId = null;
        lastRequestTokenNumber = null;

        Connection conn = Jsoup.connect(TRACKER_URL);
        conn.method(org.jsoup.Connection.Method.GET);
        Connection.Response response = conn.execute();

        lastPHPSessionId = getPhpSessionId(response);
        lastRequestTokenNumber = getTokenNumber(response.parse());
        return conn;
    }

    private Document tryGetShippingDataFromSite(Connection conn, String shNumber) throws IOException {
        try {
            return getDataFromSite(conn, shNumber);
        } catch(IOException e) {
            /*
            * Data like PHPSESSID and REQUEST_TOKEN can be out of date. Reconnect...
            * */
            conn = newConnection();
            return getDataFromSite(conn, shNumber);
        }
    }

    private Document getDataFromSite(Connection conn, String shNumber) throws IOException {
        conn.cookie("PHPSESSID", lastPHPSessionId);
        conn.data("REQUEST_TOKEN", lastRequestTokenNumber);
        conn.data("ShNumber", shNumber);
        return conn.post();
    }

    static private boolean isSessionEstablishedBefore() {
        return lastPHPSessionId != null && lastRequestTokenNumber != null;
    }

    static private String getPhpSessionId(Connection.Response response) {
        return response.cookie("PHPSESSID");
    }

    static private String getTokenNumber(Document document) {
        return document.getElementById("formularz-wyszukiwania-przesylki").getElementsByAttributeValue("name", "REQUEST_TOKEN").first().attr("value");
    }


}
