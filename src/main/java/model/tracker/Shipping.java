package model.tracker;

import com.google.common.base.Preconditions;
import model.tracker.net.Connector;
import model.tracker.net.Parser;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class Shipping {
    private ShippingMainData mainData = null;
    private List<ShippingDetailsData> detailsData = null;
    private ShipmentStatus status;

    public Shipping(String shNumber) {
        Preconditions.checkNotNull(shNumber);
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

    public ShipmentStatus getShipmentStatus() {
        return status;
    }

    public ShippingMainData getShippingMainData() {
        return mainData;
    }

    public List<ShippingDetailsData> getShippingDetailsData() {
        return detailsData;
    }

    private void isValidShippingNumberFormat(String shNumber) {
        if(!Pattern.compile("^PL[\\d]{16}$").matcher(shNumber).matches()) throw new IllegalArgumentException("Niepoprawny format numeru przesyłki");
    }
}
