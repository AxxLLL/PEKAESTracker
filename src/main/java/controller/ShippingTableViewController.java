package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class ShippingTableViewController {
    @FXML private TableView trackingTable;
    @FXML private TableColumn<TableView, String> shippingNumberColumn;
    @FXML private TableColumn<TableView, String> titleColumn;
    @FXML private TableColumn<TableView, String> statusColumn;
    @FXML private TableColumn<TableView, String> lastUpdateColumn;
}
