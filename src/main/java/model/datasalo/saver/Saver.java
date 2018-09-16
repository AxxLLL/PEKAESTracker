package model.datasalo.saver;

import model.shipment.shp.Shipping;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface Saver {
    void save(List<Shipping> listOfShipments) throws IOException;
    Path getPathToFile();
}
