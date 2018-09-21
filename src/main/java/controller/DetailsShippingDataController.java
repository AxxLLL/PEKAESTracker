package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;

public class DetailsShippingDataController {
    @FXML private TitledPane detailsDataTitledPane;
    @FXML private TableView detailsDataColumn;
    @FXML private TableColumn<TableView, String> numColumn;
    @FXML private TableColumn<TableView, String> shippingNumColumn;
    @FXML private TableColumn<TableView, String> timeColumn;
    @FXML private TableColumn<TableView, String> detailsStatusColumn;
}
