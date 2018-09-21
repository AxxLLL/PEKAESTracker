package model.tracker;

import com.google.common.base.Preconditions;
import model.shipment.shp.Shipping;
import model.shipment.shp.ShippingManager;

import java.util.ArrayList;
import java.util.List;

public class Tracker implements Runnable {

    private static int TIME_WAIT_BEFORE_NEXT_SHIPMENT_CHECK = 5; // in seconds
    private static int TIME_WAIT_BEFORE_NEXT_ALL_SHIPMENTS_CHECK = 1; // in minutes

    private List<Shipping> checkedShipments = new ArrayList<>();
    private ShippingManager shippingManager;

    public Tracker(ShippingManager shippingManager) {
        Preconditions.checkNotNull(shippingManager);
        this.shippingManager = shippingManager;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            List<Shipping> notCheckedShipments = new ArrayList<>(shippingManager.getAll());
            notCheckedShipments.removeAll(checkedShipments);
            if (notCheckedShipments.size() > 0) {
                updateShipmentData(notCheckedShipments.get(0));
                updateCheckedShipmentsList(notCheckedShipments);
            }

            if (notCheckedShipments.size() == 0) {
                checkedShipments.clear();
                Thread.sleep(TIME_WAIT_BEFORE_NEXT_ALL_SHIPMENTS_CHECK * 60 * 1000);
            } else {
                Thread.sleep(TIME_WAIT_BEFORE_NEXT_SHIPMENT_CHECK * 1000);
            }
            run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateShipmentData(Shipping shp) {
        shp.copyDataFromShipping(new Shipping(shp.getShippingNumber(), shp.getTitle()));
    }

    private void updateCheckedShipmentsList(List<Shipping> notCheckedShipments) {
        checkedShipments.add(notCheckedShipments.get(0));
        notCheckedShipments.remove(0);
    }
}
