package model.tracker;

import com.google.common.base.Preconditions;
import model.tracker.net.Connector;
import model.tracker.net.Parser;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class Shipping {
    private ShippingMainData mainData;
    private List<ShippingDetailsData> detailsData;
    private ShipmentStatus status;

    public Shipping(String shNumber) {
        Preconditions.checkNotNull(shNumber);
        Preconditions.checkArgument(Pattern.compile("^PL[\\d]{16}$").matcher(shNumber).matches());
        try {
            Parser parser = new Parser(new Connector().getShippingData(shNumber));
            mainData = parser.getMainData();
            detailsData = parser.getDetailsData();
            status = ShipmentStatus.OK;
        } catch (IllegalStateException e) {
            status = ShipmentStatus.INVALID_SHIPMENT_NUMBER;
        } catch(IOException e) {
            status = ShipmentStatus.INVALID_DATA_FORMAT;
        }
    }

    public ShipmentStatus getShipmentStatus() {
        return this.status;
    }

    public ShippingMainData getShippingMainData() {
        return this.mainData;
    }

    public List<ShippingDetailsData> getShippingDetailsData() {
        return this.detailsData;
    }
}
