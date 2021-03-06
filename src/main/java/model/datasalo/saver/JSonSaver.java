package model.datasalo.saver;

import com.google.common.base.Preconditions;
import model.datasalo.syspath.SystemPath;
import model.shipment.shp.ShipmentStatus;
import model.shipment.shp.Shipping;
import model.shipment.shp.ShippingDetailsData;
import model.shipment.shp.ShippingMainData;
import model.tracker.Tracker;
import org.json.JSONObject;
import view.ProgramStart;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JSonSaver implements Saver {

    private final Path pathToFile;
    private JSONObject jsonObject = new JSONObject();

    public JSonSaver(String fileName) throws IOException {
        Preconditions.checkNotNull(fileName, "Nazwa pliku do zapisu musi zostać określona");
        Preconditions.checkArgument(checkIfPathEndsWithValidExtension(fileName), "Plik musi być w formacie '.json'");
        pathToFile = Paths.get(SystemPath.getPath().toString(), fileName);
        createFileIfNotExists(pathToFile);
    }

    @Override
    public void save(List<Shipping> listOfShipments) throws IOException {
        Preconditions.checkNotNull(listOfShipments, "Lista przesyłek do zapisu nie może być Null'em");
        Files.write(pathToFile, prepareDataToWrite(listOfShipments));
    }

    @Override
    public Path getPathToFile() {
        return this.pathToFile;
    }

    private boolean checkIfPathEndsWithValidExtension(String fileName) {
        return fileName.endsWith(".json");
    }

    private void createFileIfNotExists(Path path) throws IOException {
        if(!Files.exists(path.getParent())) Files.createDirectories(path.getParent());
        if(!Files.exists(path)) Files.createFile(path);
    }

    private byte[] prepareDataToWrite(List<Shipping> listOfShipments) {
        StringBuilder builder = new StringBuilder(1024);
        builder.append("{");
        builder.append(getProgramSettings());
        builder.append(", ");
        builder.append("\"shipmentData\":[");

        if(listOfShipments.size() > 0) {
            listOfShipments.forEach(line -> {
                if (line.getStatus() == ShipmentStatus.OK) builder.append(parseShippingDataToJSonLine(line));
                else builder.append(parseInvalidShippingDataToJSonLine(line));
            });
        }
        if(builder.charAt(builder.length() - 1) != '[' && builder.charAt(builder.length() - 1) != '}') builder.deleteCharAt(builder.length() - 1);
        builder.append("]");
        builder.append("}");
        return builder.toString().getBytes();
    }

    private String parseInvalidShippingDataToJSonLine(Shipping shipping) {
        return String.format(System.lineSeparator() + "{\"Status\":\"Invalid\", \"Number\":\"%s\", \"Title\":\"%s\", \"LastUpdateTime\":\"%s\"},", shipping.getShippingNumber(), shipping.getTitle(), shipping.getLastUpdateTime());
    }

    private String parseShippingDataToJSonLine(Shipping shipping) {
        ShippingMainData mainData = shipping.getMainData();
        return String.format(System.lineSeparator() + "{" +
                        "\"Status\":\"Valid\"," +
                        "\"Number\":\"%s\"," +
                        "\"Title\":\"%s\"," +
                        "\"LastUpdateTime\":\"%s\"," +
                        "\"Data\": {" +
                        "\"postingTerminal\" : \"%s\"," +
                        "\"postingCountry\" : \"%s\"," +
                        "\"postingData\" : \"%s\"," +
                        "\"deliveryTerminal\" : \"%s\"," +
                        "\"deliveryCountry\" : \"%s\"," +
                        "\"deliveryType\" : \"%s\"," +
                        "\"amountOfPackages\" : \"%s\"," +
                        "\"deliveryStatus\" : \"%s\"," +
                        "\"deliveryPlace\" : \"%s\"," +
                        "\"details\" : [%s]" +
                        "}},",
                        shipping.getShippingNumber(),
                        shipping.getTitle(),
                        shipping.getLastUpdateTime(),
                        mainData.getPostingTerminal(),
                        mainData.getPostingCountry(),
                        mainData.getPostingData(),
                        mainData.getDeliveryTerminal(),
                        mainData.getDeliveryCountry(),
                        mainData.getDeliveryType(),
                        mainData.getAmountOfPackages(),
                        mainData.getDeliveryStatus(),
                        mainData.getDeliveryPlace(),
                        getDetailsShippingDataInJSonFormat(shipping.getDetailsData())
                );
    }

    private String getDetailsShippingDataInJSonFormat(List<ShippingDetailsData> detailsDataList) {
        StringBuilder builder = new StringBuilder(128);
        detailsDataList.forEach(line ->
                builder.append(String.format("{" +
                "\"packageNumber\":\"%s\"," +
                "\"time\":\"%s\"," +
                "\"status\":\"%s\"" +
                "}," ,
                        line.getPackageNumber(),
                        line.getPackageTime(),
                        line.getPackageStatus()
                ))
        );
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    private String getProgramSettings() {
        Tracker tracker = ProgramStart.getTracker();
        return String.format("\"autoUpdate\":%b, \"autoUpdateTime\":%d, \"checkIfFinished\":%b", tracker.isAutoTrackingEnabled(), tracker.getTimeBetweenRefreshes(), tracker.isCheckFinishedShipments());
    }

}
