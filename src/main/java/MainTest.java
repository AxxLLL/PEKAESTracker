import model.datasalo.saver.JSonSaver;
import model.shipment.shp.ShipmentStatus;
import model.shipment.shp.Shipping;
import model.shipment.shp.ShippingMainData;
import model.shipment.shp.ShippingManager;
import model.tracker.Tracker;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainTest {
    public static void main(String[] args) {

    }
    /*
    private static ShippingManager manager;
    private static Shipping shipping_1 = new Shipping("TEST_A", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null);
    private static Shipping shipping_2 = new Shipping("TEST_B", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null);
    private static Shipping shipping_3 = new Shipping("TEST_C", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null);
    private static Shipping shipping_4 = new Shipping("TEST_D", "", ShipmentStatus.INVALID_DATA_FORMAT, LocalDateTime.now(), null, null);

    static void init() {
        manager = new ShippingManager();
        manager.add(shipping_1);
        manager.add(shipping_2);
        manager.add(shipping_3);
        manager.add(shipping_4);
    }

    public static void main(String[] args) {
        init();
        new Tracker(manager);
    }
    */
    /*
    public static void main(String[] args) throws IOException {
        new JSonSaver("test.json").save(new ArrayList<>());




        List<Shipping> checkList = new ArrayList<>(Arrays.asList(
                //new Shipping("PL0895790004910002"),
                new Shipping("PL0895790003968002")
                //new Shipping("PL0895790003970005"),
                //new Shipping("PL0895790003970004") //Invalid number
        ));

        checkList.forEach(line -> {
            System.out.println("=======================");
            System.out.println("Status: " + line.getShipmentStatus());
            if (line.getShipmentStatus() == ShipmentStatus.OK) {
                System.out.println(line.getShippingMainData());
                line.getShippingDetailsData().forEach(detailsLine -> {
                            System.out.println("------------");
                            System.out.println(detailsLine);
                        }
                );
            }

        });

    }
    */
}