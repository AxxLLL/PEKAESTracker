package controller;

import controller.manager.ControllerManager;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import model.shipment.shp.Shipping;
import model.shipment.shp.ShippingMainData;
import view.DialogMessage;
import view.ProgramStart;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

    private Shipping shipping = null;

    public MainShippingDataController() {
        ControllerManager.add(this);
    }

    @FXML
    private void refreshShipmentData() {
        if(shipping != null) {
            Shipping shp = new Shipping(shipping.getShippingNumber(), shipping.getTitle());
            shipping.copyDataFromShipping(shp);
            updateShippingInfo(shipping);
            ((ShippingTableViewController)ControllerManager.get(ShippingTableViewController.class)).refreshTable();
        }
    }

    @FXML
    private void deleteShipmentData() {
        if(shipping != null) {
            Optional<ButtonType> response = DialogMessage.showConfirmDialog("Usunięcie przesyłki", "Czy na pewno chcesz usunąć przesyłkę o numerze " + shipping.getShippingNumber() + " z listy?");
            if (response.get() == ButtonType.OK) {
                ProgramStart.getManager().remove(shipping);
                setMainDataToDefault();
                ((ShippingTableViewController) ControllerManager.get(ShippingTableViewController.class)).refreshTable();
            }
        }
    }

    @FXML
    private void saveNewTitle() {
        if(shipping != null) {
            String newTitle = shipmentTitleInput.getText();
            shipping.setTitle(newTitle);
            ((ShippingTableViewController)ControllerManager.get(ShippingTableViewController.class)).refreshTable();
        }
    }

    TitledPane getPane() {
        return this.mainDataTitledPane;
    }

    void updateShippingInfo(Shipping shipping) {
        if(shipping == null) {
            this.shipping = null;
            return;
        }

        this.shipping = shipping;
        ShippingMainData mainData = shipping.getMainData();

        shippingNumberInputLock.setText(shipping.getShippingNumber());
        shipmentTitleInput.setText(shipping.getTitle());
        lastUpdateLabel.setText(getFormattedTime(shipping));
        shipmentStatusLabel.setText(getStatusMessage(shipping));

        deliveryPlaceLabel.setText(mainData == null ? "Brak" : mainData.getDeliveryPlace());
        amountOfPackagesLabel.setText(mainData == null ? "Brak" : mainData.getAmountOfPackages());
        deliveryTypeLabel.setText(mainData == null ? "Brak" : mainData.getDeliveryType());
        postingTerminalLabel.setText(mainData == null ? "Brak" : mainData.getPostingTerminal());
        postingCountryLabel.setText(mainData == null ? "Brak" : mainData.getPostingCountry());
        dateOfPostingLabel.setText(mainData == null ? "Brak" : mainData.getPostingData());
        deliveryTerminalLabel.setText(mainData == null ? "Brak" : mainData.getDeliveryTerminal());
        deliveryCountryLabel.setText(mainData == null ? "Brak" : mainData.getDeliveryCountry());
    }

    void setMainDataToDefault() {
        this.shipping = null;
        shippingNumberInputLock.setText("");
        shipmentTitleInput.setText("");
        lastUpdateLabel.setText("");
        shipmentStatusLabel.setText("Brak");

        deliveryPlaceLabel.setText("Brak");
        amountOfPackagesLabel.setText("Brak");
        deliveryTypeLabel.setText("Brak");
        postingTerminalLabel.setText("Brak");
        postingCountryLabel.setText("Brak");
        dateOfPostingLabel.setText("Brak");
        deliveryTerminalLabel.setText("Brak");
        deliveryCountryLabel.setText("Brak");
    }

    private String getStatusMessage(Shipping shipping) {
        switch(shipping.getStatus()) {
            case OK: {
                String msg = "Błąd (null)";
                if(shipping.getMainData() != null) {
                    msg = shipping.getMainData().getDeliveryStatus();
                    msg = msg.substring(0,1).toUpperCase() + msg.substring(1);
                }
                return msg;
            }
            case INVALID_DATA_FORMAT: return "Niepoprawny format danych";
            case INVALID_SHIPMENT_NUMBER: return "Niepoprawny numer przesyłki";
        }
        return "Błąd";
    }

    private String getFormattedTime(Shipping shipping) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd  (HH:mm)");
        return shipping.getLastUpdateTime().format(formatter);
    }

}

