package model.tracker;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import model.shipment.shp.ShipmentStatus;
import model.shipment.shp.Shipping;
import model.shipment.shp.ShippingManager;

import java.util.ArrayList;
import java.util.List;

public class Tracker extends TrackerRunner implements Runnable {


    @Getter @Setter private boolean checkFinishedShipments = false;
    @Getter private int timeBetweenRefreshes = 300; //in seconds - 5 min
    @Getter private int restTime;
    @Getter private TrackerState trackerState = TrackerState.DISABLED;

    private List<Shipping> checkedShipments = new ArrayList<>();
    private boolean active;

    public Tracker(ShippingManager manager) {
        super(manager);
    }

    public void enableAutoTracking(boolean enable) {
        changeTrackerState(enable ? TrackerState.WAITING_FOR_UPDATE : TrackerState.DISABLED);
    }

    public void setTimeBetweenRefreshes(int seconds) {
        if(seconds > 0) timeBetweenRefreshes = seconds;
    }

    public int getCheckedShipmentsListSize() {
        return checkedShipments.size();
    }





    private boolean updateShipmentData(Shipping shp) {
        if(!checkFinishedShipments) {
            if(shp.getStatus() == ShipmentStatus.OK && shp.getMainData().getDeliveryStatus().equals("przesyłka doręczona")) {
                return true;
            }
        }
        shp.copyDataFromShipping(new Shipping(shp.getShippingNumber(), shp.getTitle()));
        return false;
    }

    private void prepareBeforeWaitingForUpdate() {
        restTime = timeBetweenRefreshes;
    }

    private void prepareBeforeUpdate() {
        restTime = 0;
        checkedShipments.clear();
    }

    private void prepareBeforeDisable() {

    }

}
