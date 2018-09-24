package model.tracker;

import com.google.common.base.Preconditions;
import model.shipment.shp.Shipping;
import model.shipment.shp.ShippingManager;

import java.util.ArrayList;
import java.util.List;

public class TrackerRunner implements Runnable {
    private static final int TIME_WAIT_BEFORE_NEXT_SHIPMENT_CHECK = 2; // in seconds

    private Thread thread;
    private TrackerState trackerState;
    private ShippingManager shippingManager;
    private boolean active;
    int restTime;

    TrackerRunner(ShippingManager manager) {
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
                int timeForNextUpdateMS = 1000;
                if(isUpdateTime()) {
                    timeForNextUpdateMS = continueUpdate() * 1000;
                }

                Thread.sleep(timeForNextUpdateMS);
            } catch (InterruptedException ignored) { }
        }
    }

    public void stop() {
        this.active = false;
        changeTrackerState(TrackerState.DISABLED);
        this.thread.interrupt();
    }

    public void changeTrackerState(TrackerState newState) {
        Preconditions.checkNotNull(newState);
        if(newState == trackerState) return;
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
    }

    private boolean isUpdateTime() {
        if(trackerState != TrackerState.DISABLED && trackerState != TrackerState.UPDATING) {
            if (--restTime <= 0) {
                changeTrackerState(TrackerState.UPDATING);
                return true;
            }
        }
        return trackerState == TrackerState.UPDATING;
    }

    private int continueUpdate() {
        int timeToNextUpdate = TIME_WAIT_BEFORE_NEXT_SHIPMENT_CHECK;
        List<Shipping> notCheckedShipments = new ArrayList<>(shippingManager.getAll());
        notCheckedShipments.removeAll(checkedShipments);

        if(notCheckedShipments.size() > 0) {
            if(updateShipmentData(notCheckedShipments.get(0))) {
                timeToNextUpdate = 100;
            }
            checkedShipments.add(notCheckedShipments.get(0));
            notCheckedShipments.remove(0);
        } else {
            updatingEnd();
        }
        return timeToNextUpdate;
    }
}
