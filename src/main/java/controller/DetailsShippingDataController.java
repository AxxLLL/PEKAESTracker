package controller;

import controller.manager.ControllerManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import model.shipment.shp.Shipping;
import model.shipment.shp.ShippingDetailsData;
import view.ProgramStart;

public class DetailsShippingDataController {
    @FXML private TitledPane detailsDataTitledPane;
    @FXML private TableView<ShippingDetailsData> detailsDataColumn;
    @FXML private TableColumn<ShippingDetailsData, String> shippingNumColumn;
    @FXML private TableColumn<ShippingDetailsData, String> timeColumn;
    @FXML private TableColumn<ShippingDetailsData, String> detailsStatusColumn;

    private Shipping shipping;

    @FXML
    private void initialize() {
        PropertyValueFactory<ShippingDetailsData, String> shpNumFactory = new PropertyValueFactory<>("packageNumber");
        PropertyValueFactory<ShippingDetailsData, String> timeFactory = new PropertyValueFactory<>("packageTime");
        PropertyValueFactory<ShippingDetailsData, String> statusFactory = new PropertyValueFactory<>("packageStatus");

        shippingNumColumn.setCellValueFactory(shpNumFactory);
        timeColumn.setCellValueFactory(timeFactory);
        detailsStatusColumn.setCellValueFactory(statusFactory);
    }

    public DetailsShippingDataController() {
        ControllerManager.add(this);
    }

    void updateShippingInfo(Shipping shipping) {
        this.shipping = shipping;
        if(shipping == null) clearTableData();
        else {
            try {
                ObservableList<ShippingDetailsData> list = FXCollections.observableList(shipping.getDetailsData());
                detailsDataColumn.setItems(list);
            } catch(NullPointerException ignore) {
                clearTableData();
            }
        }
    }

    private void clearTableData() {
        detailsDataColumn.setItems(null);
    }
}
