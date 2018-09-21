package model.tracker;

import com.google.common.base.Preconditions;
import model.shipment.shp.Shipping;
import model.shipment.shp.ShippingManager;

import java.util.ArrayList;
import java.util.List;

public class Tracker implements Runnable {

    private static int TIME_WAIT_BEFORE_NEXT_SHIPMENT_CHECK = 2; // in seconds
    private static int TIME_WAIT_BEFORE_NEXT_ALL_SHIPMENTS_CHECK = 1; // in minutes

    private boolean active = true;
    private Thread thread;
    private List<Shipping> checkedShipments = new ArrayList<>();
    private ShippingManager shippingManager;

    public Tracker(ShippingManager shippingManager) {
        Preconditions.checkNotNull(shippingManager);
        this.shippingManager = shippingManager;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while(active) {
            System.out.println("TEST");
            try {
                List<Shipping> notCheckedShipments = new ArrayList<>(shippingManager.getAll());
                notCheckedShipments.removeAll(checkedShipments);
                if (notCheckedShipments.size() > 0) {
                    System.out.println("Checking " + notCheckedShipments.get(0).getShippingNumber());
                    updateShipmentData(notCheckedShipments.get(0));
                    updateCheckedShipmentsList(notCheckedShipments);
                } else System.out.println("Nothing to check...");

                if (notCheckedShipments.size() == 0) {
                    System.out.println("Waiting " + TIME_WAIT_BEFORE_NEXT_ALL_SHIPMENTS_CHECK + " minutes to next refresh...");
                    checkedShipments.clear();
                    Thread.sleep(TIME_WAIT_BEFORE_NEXT_ALL_SHIPMENTS_CHECK  * 60 * 1000);
                } else {
                    System.out.println("Waiting " + TIME_WAIT_BEFORE_NEXT_SHIPMENT_CHECK + " seconds to check next...");
                    Thread.sleep(TIME_WAIT_BEFORE_NEXT_SHIPMENT_CHECK * 1000);
                }
            } catch (InterruptedException ignored) { }
        }
    }

    synchronized public void stop() {
        this.active = false;
        thread.interrupt();
    }

    private void updateShipmentData(Shipping shp) {
        shp.copyDataFromShipping(new Shipping(shp.getShippingNumber(), shp.getTitle()));
    }

    private void updateCheckedShipmentsList(List<Shipping> notCheckedShipments) {
        checkedShipments.add(notCheckedShipments.get(0));
        notCheckedShipments.remove(0);
    }
}
