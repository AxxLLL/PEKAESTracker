package model.datasalo.saver;

import model.shipment.shp.ShipmentStatus;
import model.shipment.shp.Shipping;
import model.shipment.shp.ShippingDetailsData;
import model.shipment.shp.ShippingMainData;
import org.json.JSONArray;
import org.json.JSONObject;
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

class JSonSaverTest {

    private List<Shipping> listOfShippings = new ArrayList<>(Arrays.asList(
            createShippingDataToTestWithPrefix("TEST_A", "lineA", ShipmentStatus.OK),
            createShippingDataToTestWithPrefix("TEST_B", "lineB", ShipmentStatus.INVALID_DATA_FORMAT),
            createShippingDataToTestWithPrefix("TEST_C", "lineC", ShipmentStatus.INVALID_SHIPMENT_NUMBER),
            createShippingDataToTestWithPrefix("TEST_D", "lineD", ShipmentStatus.OK)
    ));

    @DisplayName("JSonSaver: Null value in param should throw NullPointerException")
    @Test
    void test_1() {
        assertThatNullPointerException().isThrownBy(() -> new JSonSaver(null));
    }

    @DisplayName("JSonSaver: File name without .json extension should throw IllegalArgumentException")
    @Test
    void test_2() {
        assertThatIllegalArgumentException().isThrownBy(() -> new JSonSaver("asd.txt"));
        assertThatIllegalArgumentException().isThrownBy(() -> new JSonSaver("asd"));
        assertThatIllegalArgumentException().isThrownBy(() -> new JSonSaver(""));
    }

    @DisplayName("save: Null value in param should throw NullPointerException")
    @Test
    void test_3() throws IOException {
        Saver saver = new JSonSaver("test.json");
        assertThatNullPointerException().isThrownBy(() -> saver.save(null));
    }

    @DisplayName("save: Check if data is saved correctly")
    @Test
    void test_4() throws IOException {
        Saver saver = new JSonSaver("test.json");
        saver.save(listOfShippings);
        Path path = saver.getPathToFile();
        JSONObject object = new JSONObject(readFileAsString(path));

        //------------------------------------------------
        JSONObject firstRow = object.getJSONArray("shipmentData").getJSONObject(0);
        JSONObject dataRow = firstRow.getJSONObject("Data");
        JSONArray detailsData = dataRow.getJSONArray("details");
        assertThat(firstRow.get("Status")).isEqualTo("Valid");
        assertThat(firstRow.get("Number")).isEqualTo("TEST_A");
        assertThat(dataRow.get("amountOfPackages")).isEqualTo("lineA_amountOfPackages");
        assertThat(dataRow.get("postingCountry")).isEqualTo("lineA_postingCountry");
        assertThat(dataRow.get("postingTerminal")).isEqualTo("lineA_postingTerminal");
        assertThat(dataRow.get("deliveryTerminal")).isEqualTo("lineA_deliveryTerminal");
        assertThat(dataRow.get("postingData")).isEqualTo("lineA_postingData");
        assertThat(dataRow.get("deliveryCountry")).isEqualTo("lineA_deliveryCountry");
        assertThat(dataRow.get("deliveryType")).isEqualTo("lineA_deliveryType");

        JSONObject row = detailsData.getJSONObject(0);
        assertThat(row.get("packageNumber")).isEqualTo("lineA_details_1_packageNumber");
        assertThat(row.get("time")).isEqualTo("lineA_details_1_time");
        assertThat(row.get("status")).isEqualTo("lineA_details_1_status");

        row = detailsData.getJSONObject(1);
        assertThat(row.get("packageNumber")).isEqualTo("lineA_details_2_packageNumber");
        assertThat(row.get("time")).isEqualTo("lineA_details_2_time");
        assertThat(row.get("status")).isEqualTo("lineA_details_2_status");

        row = detailsData.getJSONObject(2);
        assertThat(row.get("packageNumber")).isEqualTo("lineA_details_3_packageNumber");
        assertThat(row.get("time")).isEqualTo("lineA_details_3_time");
        assertThat(row.get("status")).isEqualTo("lineA_details_3_status");
        //------------------------------------------------
        JSONObject secondRow = object.getJSONArray("shipmentData").getJSONObject(1);
        assertThat(secondRow.get("Status")).isEqualTo("Invalid");
        assertThat(secondRow.get("Number")).isEqualTo("TEST_B");
        //------------------------------------------------
        JSONObject thirdRow = object.getJSONArray("shipmentData").getJSONObject(2);
        assertThat(thirdRow.get("Status")).isEqualTo("Invalid");
        assertThat(thirdRow.get("Number")).isEqualTo("TEST_C");
        //------------------------------------------------

        Files.deleteIfExists(path);

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

    private String readFileAsString(Path path) throws IOException {
        StringBuilder builder = new StringBuilder(512);
        List<String> reader = Files.readAllLines(path);
        for(String line : reader) {
            builder.append(line);
        }
        return builder.toString();
    }

}