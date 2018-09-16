package model.tracker.shipping;

import com.google.common.base.Preconditions;
import model.tracker.net.Connector;
import model.tracker.net.Parser;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class Shipping {
    private final String shippingNumber;
    private ShippingMainData mainData = null;
    private List<ShippingDetailsData> detailsData = null;
    private ShipmentStatus status;

    public Shipping(String shippingNumber, ShipmentStatus status, ShippingMainData mainData, List<ShippingDetailsData> detailsData) {
        Preconditions.checkNotNull(shippingNumber);
        Preconditions.checkNotNull(status);
        if(status == ShipmentStatus.OK) {
            Preconditions.checkNotNull(mainData);
            Preconditions.checkNotNull(detailsData);
        }
        this.shippingNumber = shippingNumber;
        this.status = status;
        this.mainData = mainData;
        this.detailsData = detailsData;
    }

    public Shipping(String shNumber) {
        Preconditions.checkNotNull(shNumber);
        shippingNumber = shNumber;
        try {
            isValidShippingNumberFormat(shNumber);
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

    public ShippingMainData getShippingMainData() {
        return status == ShipmentStatus.OK ? mainData : null;
    }

    public List<ShippingDetailsData> getShippingDetailsData() {
        return status == ShipmentStatus.OK ? detailsData : null;
    }

    private void isValidShippingNumberFormat(String shNumber) {
        if(!Pattern.compile("^PL[\\d]{16}$").matcher(shNumber).matches()) throw new IllegalArgumentException("Niepoprawny format numeru przesyłki");
    }
}
