package model.shipment.shp;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Główny num. przesyłki: ").append(shippingNumber).append(System.lineSeparator());
        sb.append("Terminal nadania: ").append(postingTerminal).append(System.lineSeparator());
        sb.append("Kraj nadania: ").append(postingCountry).append(System.lineSeparator());
        sb.append("Data nadania: ").append(postingData).append(System.lineSeparator());
        sb.append("Terminal dostawy: ").append(deliveryTerminal).append(System.lineSeparator());
        sb.append("Kraj dostawy: ").append(deliveryCountry).append(System.lineSeparator());
        sb.append("Rodzaj przesyłki: ").append(deliveryType).append(System.lineSeparator());
        sb.append("Liczba paczek/palet: ").append(amountOfPackages).append(System.lineSeparator());
        sb.append("Status przesyłki: ").append(deliveryStatus).append(System.lineSeparator());
        sb.append("Miejsce dostarczenia: ").append(deliveryPlace);
        return sb.toString();
    }

}
