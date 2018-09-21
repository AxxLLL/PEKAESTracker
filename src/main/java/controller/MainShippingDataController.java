package controller;

import controller.manager.ControllerManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

public class MainShippingDataController {

    @FXML private TitledPane mainDataTitledPane;
    @FXML private TextField shippingNumberInputLock;
    @FXML private TextField shipmentTitleInput;
    @FXML private Label lastUpdateLabel;
    @FXML private Label shipmentStatusLabel;
    @FXML private Label deliveryPlaceLabel;
    @FXML private Label amountOfPackagesLabel;
    @FXML private Label deliveryTypeLabel;
    @FXML private Label postingTerminalLabel;
    @FXML private Label postingCountryLabel;
    @FXML private Label dateOfPostingLabel;
    @FXML private Label deliveryTerminalLabel;
    @FXML private Label deliveryCountryLabel;

    public MainShippingDataController() {
        ControllerManager.add(this);
    }

    @FXML
    private void refreshShipmentData() {

    }

    @FXML
    private void deleteShipmentData() {

    }

    public TitledPane getPane() {
        return this.mainDataTitledPane;
    }
}

