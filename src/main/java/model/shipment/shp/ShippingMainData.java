package model.shipment.shp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ShippingMainData {
    @Getter private final String shippingNumber;
    @Getter private final String postingTerminal;
    @Getter private final String postingCountry;
    @Getter private final String postingData;
    @Getter private final String deliveryTerminal;
    @Getter private final String deliveryCountry;
    @Getter private final String deliveryType;
    @Getter private final String amountOfPackages;
    @Getter private final String deliveryStatus;
    @Getter private final String deliveryPlace;
}
