package model.tracker;

import model.shipment.shp.ShipmentStatus;
import model.shipment.shp.Shipping;
import model.shipment.shp.ShippingManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class TrackerTest {
    private ShippingManager manager;
    private Shipping shipping_1 = new Shipping("TEST_A", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null);
    private Shipping shipping_2 = new Shipping("TEST_B", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null);
    private Shipping shipping_3 = new Shipping("TEST_C", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null);
    private Shipping shipping_4 = new Shipping("TEST_D", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null);

    @BeforeEach
    void beforeEach() {
        manager = new ShippingManager();
        manager.add(shipping_1);
        manager.add(shipping_2);
        manager.add(shipping_3);
        manager.add(shipping_4);
    }

    @Test
    void test_0() {
        new Tracker(manager);
    }
}