package model.shipment.shp;

public class ShippingDetailsData {
    private final String packageNumber;
    private final String packageTime;
    private final String packageStatus;

    public ShippingDetailsData(String packageNumber, String packageTime, String packageStatus) {
        this.packageNumber = packageNumber;
        this.packageTime = packageTime;
        this.packageStatus = packageStatus;
    }

    public String getPackageNumber() {
        return packageNumber;
    }

    public String getPackageTime() {
        return packageTime;
    }

    public String getPackageStatus() {
        return packageStatus;
    }

    @Override
    public String toString() {
        return
                "Package num.: " + packageNumber + System.lineSeparator() +
                "Time: " + packageTime + System.lineSeparator() +
                "Status: " + packageStatus;
    }
}
