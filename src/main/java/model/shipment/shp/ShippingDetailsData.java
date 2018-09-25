package model.shipment.shp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
public class ShippingDetailsData {
    @Getter private final String packageNumber;
    @Getter private final String packageTime;
    @Getter private final String packageStatus;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Numer przesyłki: ").append(packageNumber).append(" | ");
        sb.append("Czas: ").append(packageTime).append(" | ");
        sb.append("Status: ").append(packageStatus);
        return sb.toString();
    }
}
