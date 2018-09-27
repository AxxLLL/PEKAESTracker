package model.shipment.shp;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import model.shipment.net.Connector;
import model.shipment.net.Parser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

public class Shipping {
    @Getter private String shippingNumber;
    @Getter private ShippingMainData mainData = null;
    @Getter private List<ShippingDetailsData> detailsData = null;
    @Getter private ShipmentStatus status;
    @Getter @Setter private String title;
    @Getter @Setter private LocalDateTime lastUpdateTime;

    public Shipping(String shNumber) {
        this(shNumber, "");
    }

    public Shipping(String shNumber, String title) {
        Preconditions.checkNotNull(shNumber, "Numer przesyłki jest nullem");
        Preconditions.checkNotNull(title, "Tytuł musi być określony (ShpNr: " + shNumber + ")");

        this.lastUpdateTime = LocalDateTime.now();
        this.shippingNumber = shNumber;
        this.title = title;

        Parser parser = parseData(shNumber);
        if(status == ShipmentStatus.OK) {
            this.mainData = parser.getMainData();
            this.detailsData = parser.getDetailsData();
        }
    }

    public Shipping(String shNumber, String title, ShipmentStatus status, LocalDateTime lastUpdateTime, ShippingMainData mainData, List<ShippingDetailsData> detailsData) {
        Preconditions.checkNotNull(shNumber, "Numer przesyłki jest nullem");
        Preconditions.checkNotNull(status, "Status przesyłki jest nullem");
        Preconditions.checkNotNull(title, "Tytuł przesyłki jest nullem");
        Preconditions.checkNotNull(lastUpdateTime, "Data ostatniego odświeżenia jest nullem");

        if(status == ShipmentStatus.OK) {
            Preconditions.checkNotNull(mainData, "Dane główne przesyłki są nullem (Status przesyłki 'OK')");
            Preconditions.checkNotNull(detailsData, "Dane szczegółowe przesyłki są nullem (Status przesyłki 'OK')");
        }

        this.shippingNumber = shNumber;
        this.status = status;
        this.title = title;
        this.lastUpdateTime = lastUpdateTime;
        this.mainData = mainData;
        this.detailsData = detailsData;
    }

    public void copyDataFromShipping(Shipping shipping) {
        Preconditions.checkArgument(shipping.getShippingNumber().equals(this.shippingNumber), "Nie można nadpisywać danych numerów przesyłki.");
        this.status = shipping.getStatus();
        this.title = shipping.getTitle();
        this.lastUpdateTime = shipping.getLastUpdateTime();
        this.mainData = shipping.getMainData();
        this.detailsData = shipping.getDetailsData();
    }

    public static boolean isValidShippingNumberFormat(String shNumber) {
        return (Pattern.compile("^PL[\\d]{16}$").matcher(shNumber).matches());
    }

    private Parser parseData(String shNumber) {
        Parser parser = null;
        try {
            Preconditions.checkArgument(isValidShippingNumberFormat(shNumber), "Niepoprawny numer przesyłki");
            parser = new Parser(new Connector(shNumber).getShippingData());
            this.status = ShipmentStatus.OK;
        } catch(IllegalArgumentException e) {
            this.status = ShipmentStatus.INVALID_SHIPMENT_NUMBER;
        } catch(NullPointerException | IOException e) {
            this.status = ShipmentStatus.INVALID_DATA_FORMAT;
        }
        return parser;
    }

}
