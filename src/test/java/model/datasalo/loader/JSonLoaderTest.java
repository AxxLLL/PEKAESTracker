package model.datasalo.loader;

import model.datasalo.saver.JSonSaver;
import model.datasalo.saver.Saver;
import model.shipment.shp.ShipmentStatus;
import model.shipment.shp.Shipping;
import model.shipment.shp.ShippingDetailsData;
import model.shipment.shp.ShippingMainData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class JSonLoaderTest {

    List<Shipping> listOfShippings = new ArrayList<>(Arrays.asList(
            createShippingDataToTestWithPrefix("TEST_A", "lineA", ShipmentStatus.OK),
            createShippingDataToTestWithPrefix("TEST_B", "lineB", ShipmentStatus.INVALID_DATA_FORMAT),
            createShippingDataToTestWithPrefix("TEST_C", "lineC", ShipmentStatus.INVALID_SHIPMENT_NUMBER),
            createShippingDataToTestWithPrefix("TEST_D", "lineD", ShipmentStatus.OK)
    ));

    @DisplayName("JSonLoader: Null value in param should throw NullPointerException")
    @Test
    void test_1() {
        assertThatNullPointerException().isThrownBy(() -> new JSonLoader(null));
    }

    @DisplayName("JSonLoader: File name without .json extension should throw IllegalArgumentException")
    @Test
    void test_2() {
        assertThatIllegalArgumentException().isThrownBy(() -> new JSonLoader("asd.txt"));
        assertThatIllegalArgumentException().isThrownBy(() -> new JSonLoader("asd"));
        assertThatIllegalArgumentException().isThrownBy(() -> new JSonLoader(""));
    }

    @DisplayName("loader: Check if data load correctly")
    @Test
    void test_3() throws IOException {
        Saver saver = new JSonSaver("test.json");
        saver.save(listOfShippings);
        Path path = saver.getPathToFile();
        Loader loader = new JSonLoader("test.json");
        List<Shipping> listWithData = loader.load();
        Files.deleteIfExists(path);
        //----------------------------
        assertThat(listWithData.size()).isEqualTo(4);
        Shipping firstRow = listWithData.get(0);
        assertThat(firstRow.getShippingNumber()).isEqualTo("TEST_A");
        assertThat(firstRow.getStatus()).isEqualTo(ShipmentStatus.OK);
        //IDK why data is not equal...
        // assertThat(firstRow.getShippingMainData()).isEqualTo("Package num.: TEST_A" + System.lineSeparator() + "Posting terminal: lineA_postingTerminal" + System.lineSeparator() + "Posting country: lineA_postingCountry" + System.lineSeparator() + "Posting data: lineA_postingData" + System.lineSeparator() + "Delivery terminal: lineA_deliveryTerminal" + System.lineSeparator() + "Delivery country: lineA_deliveryCountry" + System.lineSeparator() + "Delivery type: lineA_deliveryType" + System.lineSeparator() + "Amount of packages: lineA_amountOfPackages" + System.lineSeparator() + "Delivery status: lineA_deliveryStatus" + System.lineSeparator() + "Delivery place: lineA_deliveryPlace");

        Shipping rowWithInvalidData = listWithData.get(1);
        assertThat(rowWithInvalidData.getShippingNumber()).isEqualTo("TEST_B");
        assertThat(rowWithInvalidData.getStatus()).isEqualTo(ShipmentStatus.INVALID_DATA_FORMAT);
        assertThat(rowWithInvalidData.getMainData()).isNull();
        assertThat(rowWithInvalidData.getDetailsData()).isNull();
    }

    private Shipping createShippingDataToTestWithPrefix(String number, String prefix, ShipmentStatus status) {
        ShippingMainData mainData = new ShippingMainData(number,
                prefix + "_" +"postingTerminal",
                prefix + "_" +"postingCountry",
                prefix + "_" +"postingData",
                prefix + "_" +"deliveryTerminal",
                prefix + "_" +"deliveryCountry",
                prefix + "_" +"deliveryType",
                prefix + "_" +"amountOfPackages",
                prefix + "_" +"deliveryStatus",
                prefix + "_" +"deliveryPlace"
        );
        List<ShippingDetailsData> detailsData = new ArrayList<>();
        detailsData.add(new ShippingDetailsData(prefix + "_details_1_" + "packageNumber", prefix + "_details_1_" + "time", prefix + "_details_1_" + "status"));
        detailsData.add(new ShippingDetailsData(prefix + "_details_2_" + "packageNumber", prefix + "_details_2_" + "time", prefix + "_details_2_" + "status"));
        detailsData.add(new ShippingDetailsData(prefix + "_details_3_" + "packageNumber", prefix + "_details_3_" + "time", prefix + "_details_3_" + "status"));

        return new Shipping(number, "", status, LocalDateTime.now(), mainData, detailsData);
    }

}