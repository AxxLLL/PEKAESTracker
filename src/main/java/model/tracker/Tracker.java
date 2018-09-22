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

    private static int TIME_WAIT_BEFORE_NEXT_SHIPMENT_CHECK = 2; // in seconds
    @Getter @Setter
    private boolean checkIfFinished = false;
    @Getter
    private int timeBetweenRefreshes = 300; //in seconds - 5 min
    @Getter private int restTime;
    private Thread thread;
    @Getter private TrackerState state = TrackerState.DISABLED;
    private ShippingManager shippingManager;
    private List<Shipping> checkedShipments = new ArrayList<>();
    private boolean active;

    public Tracker(ShippingManager manager) {
        Preconditions.checkNotNull(manager);
        this.shippingManager = manager;
        active = true;
        thread = new Thread(this,"AutoRefresh thread");
        thread.start();
    }

    @Override
    public void run() {
        while(active) {
            try {
                //System.out.println("Status: " + state + " | Rest time: " + restTime);
                boolean fast = false;
                if(timerTick() == TrackerState.UPDATING) {
                    List<Shipping> notCheckedShipments = new ArrayList<>(shippingManager.getAll());
                    notCheckedShipments.removeAll(checkedShipments);

                    if(notCheckedShipments.size() > 0) {
                        fast = updateShipmentData(notCheckedShipments.get(0));
                        updateCheckedShipmentsList(notCheckedShipments);
                    } else {
                        updatingEnd();
                    }
                }

                if(!fast) Thread.sleep(state == TrackerState.UPDATING ? TIME_WAIT_BEFORE_NEXT_SHIPMENT_CHECK * 1000 : 1000);
            } catch (InterruptedException ignored) { }
        }
    }

    synchronized public void stop() {
        this.active = false;
        this.state = TrackerState.DISABLED;
        thread.interrupt();
    }

    public void disableAutoTracking(boolean disable) {
        state = disable ? TrackerState.DISABLED : TrackerState.WAITING_FOR_UPDATE;
        updatingEnd();
    }

    public void setTimeBetweenRefreshes(int time) {
        if(time > 0) timeBetweenRefreshes = time;
    }

    public void forceUpdate() {
        state = TrackerState.UPDATING;
    }

    public int getCheckedSize() {
        return checkedShipments.size();
    }

    private TrackerState timerTick() {
        if(state != TrackerState.DISABLED && state != TrackerState.UPDATING) {
            if (--restTime <= 0) {
                state = TrackerState.UPDATING;
            }
        }
        return state;
    }

    private void updatingEnd() {
        checkedShipments.clear();
        restTime = timeBetweenRefreshes;
        if(state == TrackerState.UPDATING) state = TrackerState.WAITING_FOR_UPDATE;
    }

    private boolean updateShipmentData(Shipping shp) {
        if(!checkIfFinished) {
            if(shp.getStatus() == ShipmentStatus.OK && shp.getMainData().getDeliveryStatus().equals("przesyłka doręczona")) {
                return true;
            }
        }
        shp.copyDataFromShipping(new Shipping(shp.getShippingNumber(), shp.getTitle()));
        return false;
    }

    private void updateCheckedShipmentsList(List<Shipping> notCheckedShipments) {
        checkedShipments.add(notCheckedShipments.get(0));
        notCheckedShipments.remove(0);
    }

}
