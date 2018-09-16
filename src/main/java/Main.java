import model.datasalo.saver.JSonSaver;
import model.tracker.ShipmentStatus;
import model.tracker.Shipping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        new JSonSaver("test.json").save(new ArrayList<>());

        /*
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
        */
    }
}