package model.datasalo.loader;

import com.google.common.base.Preconditions;
import model.datasalo.syspath.SystemPath;
import model.tracker.shipping.ShipmentStatus;
import model.tracker.shipping.Shipping;
import model.tracker.shipping.ShippingDetailsData;
import model.tracker.shipping.ShippingMainData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JSonLoader implements Loader {

    private final Path pathToFile;

    public JSonLoader(String fileName) {
        Preconditions.checkNotNull(fileName, "Nazwa pliku musi zostać określona");
        Preconditions.checkArgument(checkIfPathEndsWithValidExtension(fileName), "Nazwa pliku musi mieć rozszerzenie '.json'");
        pathToFile = Paths.get(SystemPath.getPath().toString(), fileName);
        Preconditions.checkState(checkIfFileExists(pathToFile), "Plik nie istnieje...");
    }

    @Override
    public List<Shipping> load() {
        List<Shipping> shippingList = new ArrayList<>();
        try {
            JSONArray mainObject = new JSONObject(readFileAsString(pathToFile)).getJSONArray("shipmentData");
            for(int i = 0; i < mainObject.length(); i ++) {
                shippingList.add(readDataForShipping(mainObject.getJSONObject(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shippingList;
    }

    @Override
    public Path getPath() {
        return this.pathToFile;
    }

    private boolean checkIfPathEndsWithValidExtension(String fileName) {
        return fileName.endsWith(".json");
    }

    private boolean checkIfFileExists(Path path) {
        return Files.exists(path);
    }

    private String readFileAsString(Path path) throws IOException {
        List<String> lineReader = Files.readAllLines(path);
        StringBuilder builder = new StringBuilder(1024);
        lineReader.forEach(builder::append);
        return builder.toString();
    }

    private Shipping readDataForShipping(JSONObject object) {
        ShipmentStatus status = getShipmentStatusByName(object.getString("Status"));
        String packageNumber = object.getString("Number");
        ShippingMainData mainData = null;
        List<ShippingDetailsData> detailsListData = null;
        if(status == ShipmentStatus.OK) {
            JSONObject jsonMainData = object.getJSONObject("Data");
            mainData = getShippingMainData(packageNumber, jsonMainData);
            detailsListData = getShippingDetailsData(jsonMainData.getJSONArray("details"));
        }
        return new Shipping(packageNumber, status, mainData, detailsListData);
    }

    private ShipmentStatus getShipmentStatusByName(String name) {
        switch(name) {
            case "Valid": return ShipmentStatus.OK;
            default: return ShipmentStatus.INVALID_DATA_FORMAT;
        }
    }

    private ShippingMainData getShippingMainData(String shNumber, JSONObject mainData) {
        return new ShippingMainData(
                shNumber,
                mainData.getString("postingTerminal"),
                mainData.getString("postingCountry"),
                mainData.getString("postingData"),
                mainData.getString("deliveryTerminal"),
                mainData.getString("deliveryCountry"),
                mainData.getString("deliveryType"),
                mainData.getString("amountOfPackages"),
                mainData.getString("deliveryStatus"),
                mainData.getString("deliveryPlace")
        );
    }

    private List<ShippingDetailsData> getShippingDetailsData(JSONArray array) {
        List<ShippingDetailsData> detailsDataList = new ArrayList<>();
        JSONObject row;
        for(int i = 0; i < array.length(); i ++) {
            row = array.getJSONObject(i);
            detailsDataList.add(new ShippingDetailsData(row.getString("packageNumber"), row.getString("time"), row.getString("status")));
        }
        return detailsDataList;
    }
}


