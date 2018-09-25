package controller;

import controller.manager.ControllerManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Popup;
import model.shipment.shp.Shipping;
import view.ProgramStart;
import view.StartFX;

import java.io.IOException;
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

        initializePopupMenu();
    }

    public TableView<Shipping> getTableView() {
        return this.trackingTable;
    }

    public void refreshTable() {
        ObservableList<Shipping> listToObserve = FXCollections.observableArrayList(ProgramStart.getManager().getAll());
        trackingTable.setItems(listToObserve);
        trackingTable.refresh();
    }

    String getStatusValueAsString(Shipping shipping) {
        switch(shipping.getStatus()) {
            case OK: {
                return shipping.getMainData() == null ? "Błąd (2)" : shipping.getMainData().getDeliveryStatus();
            }
            case INVALID_SHIPMENT_NUMBER: return "Niepoprawny numer przesyłki";
            case INVALID_DATA_FORMAT: return "Niepoprawny format danych";
        }
        return "Błąd (1)";
    }

    private String getStatusValueAsString(TableColumn.CellDataFeatures<Shipping, String> shipment) {
        return getStatusValueAsString(shipment.getValue());
    }

    private String getLastUpdateTimeAsString(TableColumn.CellDataFeatures<Shipping, String> shipment) {
        LocalDateTime time = shipment.getValue().getLastUpdateTime();
        return time.format(DateTimeFormatter.ofPattern("MM/dd/yyyy  (HH:mm)"));
    }

    private void initializePopupMenu() {
        trackingTable.setRowFactory(tableView -> {
            final TableRow<Shipping> row = new TableRow<>();

            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(getPopupContextMenu())
                            .otherwise((ContextMenu) null));

            return row;
        });
    }

    private ContextMenu getPopupContextMenu() {
        ContextMenuController contextMenuController = ((ContextMenuController)ControllerManager.get(ContextMenuController.class));
        return contextMenuController.getContextMenu();
    }
}
