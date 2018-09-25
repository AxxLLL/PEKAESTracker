package controller;

import controller.manager.ControllerManager;
import javafx.beans.binding.Bindings;
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
        initColShNum();
        initColTitle();
        initColStatus();
        initColUpdateTime();
        addListenerToTable();
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
                String msg = "Błąd (2)";
                if(shipping.getMainData() != null) {
                    msg = shipping.getMainData().getDeliveryStatus();
                    msg = msg.substring(0,1).toUpperCase() + msg.substring(1);
                }
                return msg;
            }
            case INVALID_SHIPMENT_NUMBER: return "Niepoprawny numer przesyłki";
            case INVALID_DATA_FORMAT: return "Niepoprawny format danych";
            default: return "Błąd (1)";
        }
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

    private void initColShNum() {
        PropertyValueFactory<Shipping, String> shippingNumberFactory = new PropertyValueFactory<>("shippingNumber");
        shippingNumberColumn.setCellValueFactory(shippingNumberFactory);
        shippingNumberColumn.setStyle("-fx-alignment: center;");
    }

    private void initColTitle() {
        PropertyValueFactory<Shipping, String> titleColumnFactory = new PropertyValueFactory<>("title");
        titleColumn.setCellValueFactory(titleColumnFactory);
    }

    private void initColStatus() {
        statusColumn.setCellValueFactory(shipment -> new SimpleStringProperty(getStatusValueAsString(shipment)));
        statusColumn.setStyle("-fx-alignment: center;");
    }

    private void initColUpdateTime() {
        lastUpdateColumn.setCellValueFactory(shipment -> new SimpleStringProperty(getLastUpdateTimeAsString(shipment)));
        lastUpdateColumn.setStyle("-fx-alignment: center;");
    }

    private void addListenerToTable() {
        trackingTable.getSelectionModel().selectedItemProperty().addListener((observable -> {
            Shipping shp = trackingTable.getSelectionModel().getSelectedItem();
            ((MainShippingDataController)ControllerManager.get(MainShippingDataController.class)).updateShippingInfo(shp);
            ((DetailsShippingDataController)ControllerManager.get(DetailsShippingDataController.class)).updateShippingInfo(shp);
        }));
    }
}
