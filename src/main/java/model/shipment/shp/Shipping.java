package model.shipment.shp;

import com.google.common.base.Preconditions;
import model.shipment.net.Connector;
import model.shipment.net.Parser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

public class Shipping {
    private final String shippingNumber;
    private ShippingMainData mainData = null;
    private List<ShippingDetailsData> detailsData = null;
    private ShipmentStatus status;
    private String title;
    private LocalDateTime lastUpdateTime;

    public Shipping(String shippingNumber, String title, ShipmentStatus status, LocalDateTime lastUpdateTime, ShippingMainData mainData, List<ShippingDetailsData> detailsData) {
        Preconditions.checkNotNull(shippingNumber, "Numer przesyłki jest nullem");
        Preconditions.checkNotNull(status, "Status przesyłki jest nullem");
        Preconditions.checkNotNull(title, "Tytuł przesyłki jest nullem");
        Preconditions.checkNotNull(lastUpdateTime, "Data ostatniego odświeżenia jest nullem");

        if(status == ShipmentStatus.OK) {
            Preconditions.checkNotNull(mainData, "Dane główne przesyłki są nullem (Status przesyłki 'OK')");
            Preconditions.checkNotNull(detailsData, "Dane szczegółowe przesyłki są nullem (Status przesyłki 'OK')");
        }

        this.shippingNumber = shippingNumber;
        this.status = status;
        this.title = title;
        this.lastUpdateTime = lastUpdateTime;
        this.mainData = mainData;
        this.detailsData = detailsData;
    }

    public Shipping(String shNumber) {
        this(shNumber, "");
    }

    public Shipping(String shNumber, String title) {
        Preconditions.checkNotNull(shNumber, "Numer przesyłki jest nullem");
        Preconditions.checkNotNull(title, "Tytuł musi być określony (ShpNr: " + shNumber + ")");
        lastUpdateTime = LocalDateTime.now();
        shippingNumber = shNumber;
        this.title = title;

        try {
            Preconditions.checkArgument(isValidShippingNumberFormat(shNumber), "Niepoprawny numer przesyłki");
            Parser parser = new Parser(new Connector(shNumber).getShippingData());
            mainData = parser.getMainData();
            detailsData = parser.getDetailsData();
            status = ShipmentStatus.OK;
        } catch(IllegalArgumentException e) {
            status = ShipmentStatus.INVALID_SHIPMENT_NUMBER;
        } catch(NullPointerException | IOException e) {
            status = ShipmentStatus.INVALID_DATA_FORMAT;
        }
    }

    public String getShippingNumber() {
        return shippingNumber;
    }

    public ShipmentStatus getShipmentStatus() {
        return status;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public String getTitle() {
        return this.title;
    }

    public ShippingMainData getShippingMainData() {
        return status == ShipmentStatus.OK ? mainData : null;
    }

    public List<ShippingDetailsData> getShippingDetailsData() {
        return status == ShipmentStatus.OK ? detailsData : null;
    }

    public void copyDataFromShipping(Shipping shipping) {
        Preconditions.checkArgument(shipping.getShippingNumber().equals(this.shippingNumber), "Nie można nadpisywać danych numerów przesyłki.");

        this.status = shipping.getShipmentStatus();
        this.title = shipping.getTitle();
        this.lastUpdateTime = shipping.getLastUpdateTime();
        this.mainData = shipping.getShippingMainData();
        this.detailsData = shipping.getShippingDetailsData();
    }

    private boolean isValidShippingNumberFormat(String shNumber) {
        return (Pattern.compile("^PL[\\d]{16}$").matcher(shNumber).matches());
    }
}
