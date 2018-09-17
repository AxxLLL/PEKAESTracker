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
        Preconditions.checkNotNull(shippingNumber);
        Preconditions.checkNotNull(status);
        Preconditions.checkNotNull(title);
        Preconditions.checkNotNull(lastUpdateTime);
        if(status == ShipmentStatus.OK) {
            Preconditions.checkNotNull(mainData);
            Preconditions.checkNotNull(detailsData);
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
        Preconditions.checkNotNull(shNumber);
        Preconditions.checkNotNull(title);
        lastUpdateTime = LocalDateTime.now();
        shippingNumber = shNumber;
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

    private boolean isValidShippingNumberFormat(String shNumber) {
        return (Pattern.compile("^PL[\\d]{16}$").matcher(shNumber).matches());
    }
}
