package controller;

import controller.manager.ControllerManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.shipment.shp.Shipping;
import view.ProgramStart;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ShippingTableViewController {
    @FXML private TableView<Shipping> trackingTable;
    @FXML private TableColumn<Shipping, String> shippingNumberColumn;
    @FXML private TableColumn<Shipping, String> titleColumn;
    @FXML private TableColumn<Shipping, String> statusColumn;
    @FXML private TableColumn<Shipping, String> lastUpdateColumn;

    public ShippingTableViewController() {
        ControllerManager.add(this);
    }

    @FXML
    void initialize() {
        PropertyValueFactory<Shipping, String> shippingNumberFactory = new PropertyValueFactory<>("shippingNumber");
        shippingNumberColumn.setCellValueFactory(shippingNumberFactory);
        shippingNumberColumn.setStyle("-fx-alignment: center;");

        PropertyValueFactory<Shipping, String> titleColumnFactory = new PropertyValueFactory<>("title");
        titleColumn.setCellValueFactory(titleColumnFactory);

        statusColumn.setCellValueFactory(shipment -> new SimpleStringProperty(getStatusValueAsString(shipment)));
        statusColumn.setStyle("-fx-alignment: center;");

        lastUpdateColumn.setCellValueFactory(shipment -> new SimpleStringProperty(getLastUpdateTimeAsString(shipment)));
        lastUpdateColumn.setStyle("-fx-alignment: center;");

        trackingTable.getSelectionModel().selectedItemProperty().addListener((observable -> {
            Shipping shp = trackingTable.getSelectionModel().getSelectedItem();
            ((MainShippingDataController)ControllerManager.get(MainShippingDataController.class)).updateShippingInfo(shp);
            ((DetailsShippingDataController)ControllerManager.get(DetailsShippingDataController.class)).updateShippingInfo(shp);
        }));
    }

    public void refreshTable() {
        ObservableList<Shipping> listToObserve = FXCollections.observableArrayList(ProgramStart.getManager().getAll());
        trackingTable.setItems(listToObserve);
        trackingTable.refresh();
    }

    private String getStatusValueAsString(TableColumn.CellDataFeatures<Shipping, String> shipment) {
        switch(shipment.getValue().getStatus()) {
            case OK: {
                return shipment.getValue().getMainData() == null ? "Błąd (2)" : shipment.getValue().getMainData().getDeliveryStatus();
            }
            case INVALID_SHIPMENT_NUMBER: return "Niepoprawny numer przesyłki";
            case INVALID_DATA_FORMAT: return "Niepoprawny format danych";
        }
        return "Błąd (1)";
    }

    private String getLastUpdateTimeAsString(TableColumn.CellDataFeatures<Shipping, String> shipment) {
        LocalDateTime time = shipment.getValue().getLastUpdateTime();
        return time.format(DateTimeFormatter.ofPattern("MM/dd/yyyy  (HH:mm)"));
    }
}
