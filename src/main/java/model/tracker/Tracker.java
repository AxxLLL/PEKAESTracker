package model.tracker;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import model.shipment.shp.ShipmentStatus;
import model.shipment.shp.Shipping;
import model.shipment.shp.ShippingManager;

import java.util.ArrayList;
import java.util.List;

public class Tracker implements Runnable {
    private static final int TIME_WAIT_BEFORE_NEXT_SHIPMENT_CHECK = 2; // in seconds

    private Thread thread;
    private ShippingManager shippingManager;
    private List<Shipping> checkedShipments = new ArrayList<>();
    private boolean active;
    @Getter private TrackerState trackerState = TrackerState.WAITING_FOR_UPDATE;
    @Getter private int timeToNextUpdate = 10;
    @Getter private int timeBetweenRefreshes = 300; //in seconds - 5 min
    @Getter @Setter private boolean checkFinishedShipments = false;

    public Tracker(ShippingManager manager) {
        Preconditions.checkNotNull(manager);
        this.shippingManager = manager;
        this.active = true;
        this.thread = new Thread(this, "AutoRefresh thread");
        this.thread.start();
    }

    @Override
    public void run() {
        while(active) {
            try {
                //System.out.println("Status: " + state + " | Rest time: " + restTime);
                int timeForNextUpdateMS = !isUpdateTime() ? 1000 : (continueUpdate() * 1000);
                if(timeForNextUpdateMS != 0) Thread.sleep(timeForNextUpdateMS);
            } catch (InterruptedException ignored) { }
        }
    }

    public void stop() {
        this.active = false;
        changeTrackerState(TrackerState.DISABLED);
        this.thread.interrupt();
    }

    public boolean changeTrackerState(TrackerState newState) {
        Preconditions.checkNotNull(newState);
        if(newState == trackerState) return false;
        switch(newState) {
            case UPDATING:
                prepareBeforeUpdate();
                break;
            case WAITING_FOR_UPDATE:
                prepareBeforeWaitingForUpdate();
                break;
            case DISABLED:
                prepareBeforeDisable();
                break;
        }
        trackerState = newState;
        return true;
    }

    public boolean enableAutoTracking(boolean enable) {
        return changeTrackerState(enable ? TrackerState.WAITING_FOR_UPDATE : TrackerState.DISABLED);
    }

    public boolean isAutoTrackingEnabled() {
        return (trackerState != TrackerState.DISABLED);
    }

    public boolean updateShipmentData(Shipping shp) {
        return updateShipmentData(shp, true);
    }

    public int getCheckedListSize() {
        return this.checkedShipments.size();
    }

    public void setTimeBetweenRefreshes(int minutes) {
        if(minutes > 0) timeBetweenRefreshes = minutes * 60;
    }

    private boolean updateShipmentData(Shipping shp, boolean force) {
        if(!checkFinishedShipments && !force) {
            if(shp.getStatus() == ShipmentStatus.OK && shp.getMainData().getDeliveryStatus().equals("przesyłka doręczona")) {
                return false;
            }
        }
        shp.copyDataFromShipping(new Shipping(shp.getShippingNumber(), shp.getTitle()));
        return true;
    }

    private boolean isUpdateTime() {
        if(trackerState == TrackerState.WAITING_FOR_UPDATE) {
            if ((--timeToNextUpdate) <= 0) return changeTrackerState(TrackerState.UPDATING);
        }
        return (trackerState == TrackerState.UPDATING);
    }

    private int continueUpdate() {
        int timeToNextUpdate = TIME_WAIT_BEFORE_NEXT_SHIPMENT_CHECK;
        List<Shipping> notCheckedShipments = new ArrayList<>(shippingManager.getAll());
        notCheckedShipments.removeAll(checkedShipments);
        Shipping shp = notCheckedShipments.isEmpty() ? null : notCheckedShipments.get(0);

        if(shp != null) {
            if(!updateShipmentData(shp, false)) timeToNextUpdate = 0;
            checkedShipments.add(shp);
            notCheckedShipments.remove(shp);
        } else changeTrackerState(TrackerState.WAITING_FOR_UPDATE);

        return timeToNextUpdate;
    }

    private void prepareBeforeWaitingForUpdate() {
        timeToNextUpdate = timeBetweenRefreshes;
    }

    private void prepareBeforeUpdate() {
        timeToNextUpdate = 0;
        checkedShipments.clear();
    }

    private void prepareBeforeDisable() {
        timeToNextUpdate = 0;
    }
}
