package model.shipment.shp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ShippingDetailsData {
    @Getter private final String packageNumber;
    @Getter private final String packageTime;
    @Getter private final String packageStatus;
}
